package com.github.javadev.undescriptive.client;

import com.github.javadev.undescriptive.protocol.request.SolutionRequest;
import com.github.javadev.undescriptive.protocol.response.GameCounters;
import com.github.javadev.undescriptive.protocol.response.GameResponse;
import com.github.javadev.undescriptive.protocol.response.GameResponseItem;
import com.github.javadev.undescriptive.protocol.response.SolutionResponse;
import com.github.javadev.undescriptive.protocol.response.WeatherResponse;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public interface GameClient {
    void close();

    void closeAsynchronously();

    ListenableFuture<GameResponse> getGame();

    ListenableFuture<WeatherResponse> getWeather(Integer id);

    ListenableFuture<SolutionResponse> sendSolution(Integer id, SolutionRequest solutionRequest);

    SolutionRequest generateGameSolution(GameResponseItem gameResponseItem, WeatherResponse weatherResponse);

    GameCounters getAndSolveGames(int amountOfGames);
}
