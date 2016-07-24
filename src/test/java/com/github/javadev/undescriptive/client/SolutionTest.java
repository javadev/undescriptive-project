package com.github.javadev.undescriptive.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.response.GameCounters;
import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import org.junit.Test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final GameClient CLIENT = AsyncClient.createDefault();

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
        GameCounters gameCounters = CLIENT.getAndSolveGames(20000);
        System.out.println("victoryCount - " + gameCounters.getVictoryCount().get());
        System.out.println("errorCount - " + gameCounters.getErrorCount().get());
        assertEquals("victoryCount + stormCount + errorCount should be 20000", 20000,
            gameCounters.getVictoryCount().get() + gameCounters.getStormCount().get()
            + gameCounters.getErrorCount().get());
    }
}
