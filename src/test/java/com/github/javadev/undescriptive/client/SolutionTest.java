package com.github.javadev.undescriptive.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.github.javadev.undescriptive.protocol.response.WeatherResponse;
import com.github.javadev.undescriptive.protocol.response.SolutionResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    protected final static AsyncClient CLIENT = AsyncClient.createDefault();

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
        final AtomicInteger errorCount = new AtomicInteger();
        final ExecutorService executor = Executors.newFixedThreadPool(100);
        final List<Callable<Void>> list = new ArrayList<Callable<Void>>();
        for (int gameIndex = 0; gameIndex < 20000; gameIndex += 1) {
            list.add(new CallableImpl(victoryCount, stormCount, errorCount));
        }
        executor.invokeAll(list);
        executor.shutdown();
        System.out.println("victoryCount - " + victoryCount.get());
        System.out.println("errorCount - " + errorCount.get());
        assertEquals("victoryCount + stormCount + errorCount should be 20000", 20000,
            victoryCount.get() + stormCount.get() + errorCount.get());
    }

    private static class CallableImpl implements Callable<Void> {

        private final AtomicInteger victoryCount;
        private final AtomicInteger stormCount;
        private final AtomicInteger errorCount;

        public CallableImpl(AtomicInteger victoryCount, AtomicInteger stormCount, AtomicInteger errorCount) {
            this.victoryCount = victoryCount;
            this.stormCount = stormCount;
            this.errorCount = errorCount;
        }

        public Void call() {
            try {
                final GameResponse game = CLIENT.getGame().get();
                game.toString();
                game.getGameResponseItem();
                game.getGameResponseItem().getName();
                game.getGameResponseItem().getAttack();
                game.getGameResponseItem().getArmor();
                game.getGameResponseItem().getAgility();
                game.getGameResponseItem().getEndurance();
                game.getGameResponseItem().toString();
                final WeatherResponse weatherResponse = CLIENT.getWeather(game.getGameId()).get();
                weatherResponse.getTime();
                weatherResponse.getCode();
                weatherResponse.getMessage();
                weatherResponse.toString();
                if ("SRO".equals(weatherResponse.getCode())) {
                    stormCount.getAndIncrement();
                } else {
                    final SolutionRequest request = CLIENT.generateGameSolution(game.getGameResponseItem(), weatherResponse);
                    request.toString();
                    SolutionResponse response = CLIENT.sendSolution(game.getGameId(), request).get();
                    response.getStatus();
                    response.getMessage();
                    response.toString();
                    if ("Victory".equals(response.getStatus())) {
                        victoryCount.getAndIncrement();
                    }
                }
            } catch (Exception ex) {
                errorCount.getAndIncrement();
            }
            return null; }
    }
}
