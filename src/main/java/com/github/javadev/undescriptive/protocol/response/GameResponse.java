package com.github.javadev.undescriptive.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResponse {
    @JsonProperty private final Integer gameId;
    @JsonProperty private final GameResponseItem gameResponseItem;

    public GameResponse(@JsonProperty("gameId") final Integer gameId,
        @JsonProperty("knight") final GameResponseItem gameResponseItem) {
        this.gameId = gameId;
        this.gameResponseItem = gameResponseItem;
    }

    public Integer getGameId() {
        return gameId;
    }

    public GameResponseItem getGameResponseItem() {
        return gameResponseItem;
    }

    @Override
    public String toString() {
        return "GameResponse{" +
            "gameId=" + gameId +
            ", knight={" + gameResponseItem + "}"
            + '}';
    }
}
