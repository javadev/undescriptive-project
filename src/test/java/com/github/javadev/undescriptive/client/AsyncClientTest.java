package com.github.javadev.undescriptive.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.FluentStringsMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncClientTest {
    private static final String BASE_URL = "http://www.dragonsofmugloar.com/api/game";
    @Mock private AsyncHttpClient httpClient;
    @Mock private AsyncHttpClient.BoundRequestBuilder builder;

    private AsyncClient client;

    @Before
    public void setUp() throws Exception {
        final Constructor<AsyncClient> ctor = AsyncClient.class.getDeclaredConstructor(
            AsyncHttpClient.class,
            String.class);

        ctor.setAccessible(true);
        this.client = ctor.newInstance(httpClient, BASE_URL);

        when(httpClient.prepareGet(anyString())).thenReturn(builder);
        when(builder.setQueryParameters(any(FluentStringsMap.class))).thenReturn(builder);

        AsyncClient.create(new AsyncHttpClientConfig.Builder().build());
    }

    @Test(expected = ExecutionException.class)
    public void requestIOExceptionTest() throws Exception {
        when(builder.execute(any(AsyncHandler.class))).thenThrow(new IOException());

        final ListenableFuture<GameResponse> response = client.getGame();
        response.get();
    }

    @Test
    public void closeTest() throws Exception {
        client.close();
    }

    @Test
    public void closeAsynchronouslyTest() throws Exception {
        client.closeAsynchronously();
    }
}

