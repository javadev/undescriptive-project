package com.github.javadev.undescriptive.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponse {
    @JsonProperty private final String time;
    @JsonProperty private final String code;
    @JsonProperty private final String message;

    public WeatherResponse(@JsonProperty("time") final String time,
        @JsonProperty("code") final String code,
        @JsonProperty("message") final String message) {
        this.time = time;
        this.code = code;
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
            "time=" + time
            + ", code=" + code
            + ", message=" + message
            + '}';
    }
}
