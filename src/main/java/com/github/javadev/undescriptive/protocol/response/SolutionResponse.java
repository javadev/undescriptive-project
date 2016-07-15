package com.github.javadev.undescriptive.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SolutionResponse {
    @JsonProperty private final String status;
    @JsonProperty private final String message;

    public SolutionResponse(@JsonProperty("status") final String status,
        @JsonProperty("message") final String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SolutionResponse{" +
            "status=" + status +
            ", message=" + message
            + '}';
    }
}
