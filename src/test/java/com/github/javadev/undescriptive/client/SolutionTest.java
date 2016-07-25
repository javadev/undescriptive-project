package com.github.javadev.undescriptive.client;

import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.response.GameCounters;
import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import com.github.javadev.undescriptive.protocol.response.GameResponseItem;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.github.javadev.undescriptive.protocol.response.SolutionResponse;
import com.github.javadev.undescriptive.protocol.response.WeatherResponse;
import org.junit.Test;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static GameClient client = AsyncClient.createDefault();

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
    public void testResponse() throws Exception {
        GameResponseItem gameResponseItem = new GameResponseItem("name", 1, 1, 1, 1);
        gameResponseItem.getName();
        gameResponseItem.getAttack();
        gameResponseItem.getArmor();
        gameResponseItem.getAgility();
        gameResponseItem.getEndurance();
        gameResponseItem.toString();
        GameResponse gameResponse = new GameResponse(1, gameResponseItem);
        gameResponse.getGameId();
        gameResponse.getGameResponseItem();
        gameResponse.toString();
        SolutionResponse solutionResponse = new SolutionResponse("", "");
        solutionResponse.getStatus();
        solutionResponse.getMessage();
        solutionResponse.toString();
        WeatherResponse weatherResponse = new WeatherResponse("", "", "", "", "");
        weatherResponse.getTime();
        weatherResponse.getCode();
        weatherResponse.getMessage();
        weatherResponse.toString();
    }

    @Test
    public void testSolution() throws Exception {
        GameCounters gameCounters = client.getAndSolveGames(20000);
        System.out.println("victoryCount - " + gameCounters.getVictoryCount().get());
        System.out.println("errorCount - " + gameCounters.getErrorCount().get());
        assertEquals("victoryCount + stormCount + errorCount should be 20000", 20000,
            gameCounters.getVictoryCount().get() + gameCounters.getStormCount().get()
            + gameCounters.getErrorCount().get());
    }
}
