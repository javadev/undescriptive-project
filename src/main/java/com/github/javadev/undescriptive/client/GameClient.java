package com.github.javadev.undescriptive.client;

import java.util.Map;

public interface GameClient {
    Map<String, Object> getGame();

    Map<String, Object> getWeather(Long id);

    Map<String, Object> sendSolution(Long id, Map<String, Object> solutionRequest);

    Map<String, Object> generateGameSolution(Map<String, Object> gameResponseItem, Map<String, Object> weatherResponse);

    GameCounters getAndSolveGames(int amountOfGames);
}
