package com.github.javadev.undescriptive.client;

import ch.qos.logback.classic.Level;
import com.github.javadev.undescriptive.protocol.request.GameRequest;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
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
        final GameRequest request = new GameRequest();
        request.scale = 5;
        request.claw = 5;
        request.wing = 5;
        request.fire = 5;
        client.putGame(game.getGameId(), request).get();
    }
}
