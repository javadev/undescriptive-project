package com.github.javadev.undescriptive.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.*;
import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.github.javadev.undescriptive.protocol.response.WeatherResponse;
import com.github.javadev.undescriptive.protocol.response.SolutionResponse;
import org.junit.Test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    protected final static AsyncClient client = AsyncClient.createDefault();

    @BeforeClass
    public static void beforeClass() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
    }

    @Test
    public void testRequest() throws Exception {
        final SolutionRequest request = SolutionRequest.builder()
            .scale(5)
            .claw(5)
            .wing(5)
            .fire(5)
            .build();
        request.getScale();
        request.getClaw();
        request.getWing();
        request.getFire();
    }

    @Test
    public void testSolution() throws Exception {
        final AtomicInteger victoryCount = new AtomicInteger();
        final AtomicInteger stormCount = new AtomicInteger();
        final ExecutorService executor = Executors.newFixedThreadPool(100);
        final List<Callable<Object>> list = new ArrayList<Callable<Object>>();
        for (int gameIndex = 0; gameIndex < 100000; gameIndex += 1) {
        list.add(new Callable<Object>() { public Object call() {
            try {
            final GameResponse game = client.getGame().get();
            game.toString();
            game.getGameResponseItem();
            game.getGameResponseItem().getName();
            game.getGameResponseItem().getAttack();
            game.getGameResponseItem().getArmor();
            game.getGameResponseItem().getAgility();
            game.getGameResponseItem().getEndurance();
            game.getGameResponseItem().toString();
            final WeatherResponse weatherResponse = client.getWeather(game.getGameId()).get();
            weatherResponse.getTime();
            weatherResponse.getCode();
            weatherResponse.getMessage();
            weatherResponse.toString();
            final SolutionRequest request = client.generateGameSolution(game.getGameResponseItem(), weatherResponse);
            request.toString();
            SolutionResponse response = client.sendSolution(game.getGameId(), request).get();
            response.getStatus();
            response.getMessage();
            response.toString();
            if ("Victory".equals(response.getStatus())) {
                victoryCount.getAndIncrement();
            } else if ("SRO".equals(weatherResponse.getCode())) {
                stormCount.getAndIncrement();
            }
            } catch (Exception ex) {
            }
            return null; } });
        }
        executor.invokeAll(list);
        System.out.println("victoryCount - " + victoryCount.get());
        assertEquals("victoryCount + stormCount should be 100000", 100000, victoryCount.get() + stormCount.get());
    }
}
