package com.github.javadev.undescriptive.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.github.javadev.undescriptive.ApiException;
import com.github.javadev.undescriptive.protocol.request.*;
import com.github.javadev.undescriptive.protocol.response.*;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm;
import com.ning.http.client.Response;

import java.io.IOException;
import java.util.Map;

public class AsyncClient {
    private static final String BASE_URL = "http://www.dragonsofmugloar.com";
    private static final String WEATHER_URL = "/weather/api/report/";

    private final static ObjectMapper MAPPER = new ObjectMapper()
        .registerModule(new JodaModule())
        .registerModule(new SimpleModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final static XmlMapper XML_MAPPER = new XmlMapper();

    private final AsyncHttpClient httpClient;
    private final String baseUrl;

    private AsyncClient(
            final AsyncHttpClient httpClient,
            final String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    private static AsyncHttpClientConfig commonSetup(final AsyncHttpClientConfig.Builder configBuilder) {
        final Realm realm = new Realm.RealmBuilder().build();
        configBuilder.setRealm(realm);
        return configBuilder.build();
    }

    public static AsyncClient createDefault() {
        return new AsyncClient(
            new AsyncHttpClient(commonSetup(new Builder())), BASE_URL);
    }

    public static AsyncClient create(final AsyncHttpClientConfig config) {
        return new AsyncClient(
            new AsyncHttpClient(commonSetup(new Builder(config))), BASE_URL);
    }

    public void close() {
        this.httpClient.close();
    }

    public void closeAsynchronously() {
        this.httpClient.closeAsynchronously();
    }

    private BoundRequestBuilder get(final String resourceUrl) {
        return this.httpClient.prepareGet(this.baseUrl + resourceUrl);
    }

    private BoundRequestBuilder put(final String resourceUrl, final HasParams hasParams) {
        final BoundRequestBuilder builder = this.httpClient.preparePut(this.baseUrl + resourceUrl);
        final Map<String, Object> params = hasParams.getParams();
        try {
            final String objectAsString = MAPPER.writeValueAsString(params);
            builder.addHeader("Content-Type", "application/json; charset=utf-8");
            builder.setBody(objectAsString); 
        } catch (Exception ignore) {            
        }
        return builder;
    }

    public ListenableFuture<GameResponse> getGame() {
        return execute(GameResponse.class, get("/api/game"));
    }

    public ListenableFuture<SolutionResponse> putGame(Integer id, SolutionRequest gameRequest) {
        return execute(SolutionResponse.class, put("/api/game/" + id + "/solution", gameRequest));
    }

    public ListenableFuture<WeatherResponse> getWeather(Integer id) {
        return execute(WeatherResponse.class, get(WEATHER_URL + id));
    }

    private static <T> ListenableFuture<T> execute(
            final Class<T> clazz,
            final BoundRequestBuilder request) {
        final SettableFuture<T> guavaFut = SettableFuture.create();
        try {
            request.execute(new GuavaFutureConverter<T>(clazz, guavaFut));
        }
        catch (final IOException e) {
            guavaFut.setException(e);
        }
        return guavaFut;
    }

    private static class GuavaFutureConverter<T> extends AsyncCompletionHandler<T> {
        final Class<T> clazz;
        final SettableFuture<T> guavaFut;

        public GuavaFutureConverter(
                final Class<T> clazz,
                final SettableFuture<T> guavaFut) {
            this.clazz = clazz;
            this.guavaFut = guavaFut;
        }

        private static boolean isSuccess(final Response response) {
            final int statusCode = response.getStatusCode();
            return (statusCode > 199 && statusCode < 400);
        }

        @Override
        public void onThrowable(final Throwable t) {
            guavaFut.setException(t);
        }

        @Override
        public T onCompleted(final Response response) throws Exception {
            if (isSuccess(response)) {
                final T value = clazz == WeatherResponse.class ? XML_MAPPER.readValue(response.getResponseBody(), clazz)
                    : MAPPER.readValue(response.getResponseBody(), clazz);
                guavaFut.set(value);
                return value;
            } else {
                final ErrorResponse error = MAPPER.readValue(response.getResponseBody(), ErrorResponse.class);
                final ApiException exception = new ApiException(response.getUri(), error);
                guavaFut.setException(exception);
                throw exception;
            }
        }
    }
}
