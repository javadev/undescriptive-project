package com.github.javadev.undescriptive.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AsyncClient {
    private static final String BASE_URL = "http://www.dragonsofmugloar.com";
    private static final String WEATHER_URL = "/weather/api/report/";

    private final static ObjectMapper MAPPER = new ObjectMapper()
        .registerModule(new JodaModule())
        .registerModule(new SimpleModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final static XmlMapper XML_MAPPER = new XmlMapper();

    private static final int[][] SOLUTIONS = {
        {8, 8, 4, 0}, {10, 6, 3, 1},
        {8, 7, 5, 0}, {10, 5, 4, 1},
        {8, 6, 6, 0}, {10, 4, 4, 2},
        {7, 7, 6, 0}, {10, 4, 4, 2},
        {8, 8, 3, 1}, {10, 6, 2, 2},
        {8, 7, 4, 1}, {10, 5, 4, 1},
        {8, 6, 5, 1}, {10, 4, 5, 1},
        {7, 7, 5, 1}, {9, 5, 4, 2},
        {7, 6, 6, 1}, {9, 4, 4, 3},
        {8, 8, 2, 2}, {10, 6, 3, 1},
        {8, 7, 3, 2}, {10, 5, 4, 1},
        {8, 6, 4, 2}, {10, 4, 3, 3},
        {7, 7, 4, 2}, {10, 5, 4, 1},
        {8, 5, 5, 2}, {10, 5, 4, 1},
        {7, 6, 5, 2}, {10, 4, 4, 2},
        {6, 6, 6, 2}, {10, 4, 3, 3},
        {8, 6, 3, 3}, {10, 4, 4, 2},
        {7, 7, 3, 3}, {10, 4, 4, 2},
        {8, 5, 4, 3}, {10, 4, 4, 2},
        {7, 6, 4, 3}, {9, 4, 5, 2},
        {7, 5, 5, 3}, {9, 5, 4, 2},
        {6, 6, 5, 3}, {8, 5, 4, 3},
        {8, 4, 4, 4}, {10, 4, 3, 3},
        {7, 5, 4, 4}, {10, 4, 3, 3},
        {6, 6, 4, 4}, {10, 4, 3, 3},
        {6, 5, 5, 4}, {10, 4, 3, 3},
        {5, 5, 5, 5}, {10, 4, 3, 3}
        };

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

    public ListenableFuture<SolutionResponse> sendSolution(Integer id, SolutionRequest solutionRequest) {
        return execute(SolutionResponse.class, put("/api/game/" + id + "/solution", solutionRequest));
    }

    public SolutionRequest generateGameSolution(GameResponseItem gameResponseItem, WeatherResponse weatherResponse) {
        if ("T E".equals(weatherResponse.getCode())) {
            return SolutionRequest.builder()
                .scale(5)
                .claw(5)
                .wing(5)
                .fire(5)
                .build();
        } else if ("HVA".equals(weatherResponse.getCode())) {
            return SolutionRequest.builder()
                .scale(10)
                .claw(10)
                .wing(0)
                .fire(0)
                .build();
        }

        final List<Integer> knightAttrs = Arrays.asList(gameResponseItem.getAttack(),
            gameResponseItem.getArmor(), gameResponseItem.getAgility(), gameResponseItem.getEndurance());
        final Integer[] indexes = { 0, 1, 2, 3 };
        
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override public int compare(final Integer o1, final Integer o2) {
                return knightAttrs.get(o1).compareTo(knightAttrs.get(o2));
            }
        });
        int maxIndex = indexes[3];
        int secondMaxIndex = indexes[2];
        int thirdMaxIndex = indexes[1];
        int forthMaxIndex = indexes[0];
        int[] dragonAttrs = new int[] {0, 0, 0, 0};
        for (int index = 0; index < SOLUTIONS.length; index += 2) {
            if (knightAttrs.get(maxIndex) == SOLUTIONS[index][0]
                && knightAttrs.get(secondMaxIndex) == SOLUTIONS[index][1]
                && knightAttrs.get(thirdMaxIndex) == SOLUTIONS[index][2]
                && knightAttrs.get(forthMaxIndex) == SOLUTIONS[index][3]) {
                dragonAttrs[maxIndex] = SOLUTIONS[index + 1][0];
                dragonAttrs[secondMaxIndex] = SOLUTIONS[index + 1][1];
                dragonAttrs[thirdMaxIndex] = SOLUTIONS[index + 1][2];
                dragonAttrs[forthMaxIndex] = SOLUTIONS[index + 1][3];
                break;
            }
        }
        final SolutionRequest request = SolutionRequest.builder()
            .scale(dragonAttrs[0])
            .claw(dragonAttrs[1])
            .wing(dragonAttrs[2])
            .fire(dragonAttrs[3])
            .build();
        return request;
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
        private final Class<T> clazz;
        private final SettableFuture<T> guavaFut;

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
                throw new UnsupportedOperationException(response.getResponseBody());
            }
        }
    }

    public static void main(String ... args) {
        final String message = "Dragons of Mugloar solution.\n\n"
            + "For docs, license, tests, and downloads, see: https://github.com/javadev/undescriptive-project";
        System.out.println(message);
    }
}
