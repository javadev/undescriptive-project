package com.github.javadev.undescriptive.client;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.FluentStringsMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsyncClientTest {
    private static final String BASE_URL = "http://www.dragonsofmugloar.com/api/game";
    @Mock private AsyncHttpClient httpClient;
    @Mock private AsyncHttpClient.BoundRequestBuilder builder;

    private AsyncClient client;

    @BeforeClass
    public static void beforeClass() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
    }

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

    @Test
    public void closeTest() throws Exception {
        client.close();
    }

    @Test
    public void closeAsynchronouslyTest() throws Exception {
        client.closeAsynchronously();
    }
}

