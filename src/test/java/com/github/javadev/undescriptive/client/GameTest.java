package com.github.javadev.undescriptive.client;

import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.github.javadev.undescriptive.protocol.response.WeatherResponse;
import com.github.javadev.undescriptive.protocol.response.SolutionResponse;
import org.junit.Test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTest {
    protected final static AsyncClient client = AsyncClient.createDefault();

    @BeforeClass
    public static void beforeClass() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
    }

    @Test
    public void testGame() throws Exception {
        final GameResponse game = client.getGame().get();
        System.out.println(game);
        final SolutionRequest request = SolutionRequest.builder()
            .scale(5)
            .claw(5)
            .wing(5)
            .fire(5)
            .build();
        WeatherResponse weatherResponse = client.getWeather(game.getGameId()).get();
        System.out.println(weatherResponse);
        SolutionResponse response = client.putGame(game.getGameId(), request).get();
        System.out.println(response);
    }
}
