package com.github.javadev.undescriptive.client;

import java.util.concurrent.atomic.AtomicInteger;

public class GameCounters {
    private final AtomicInteger victoryCount = new AtomicInteger();
    private final AtomicInteger stormCount = new AtomicInteger();
    private final AtomicInteger errorCount = new AtomicInteger();

    public AtomicInteger getVictoryCount() {
        return victoryCount;
    }

    public AtomicInteger getStormCount() {
        return stormCount;
    }

    public AtomicInteger getErrorCount() {
        return errorCount;
    }
}
