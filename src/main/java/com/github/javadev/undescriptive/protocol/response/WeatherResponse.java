package com.github.javadev.undescriptive.protocol.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JacksonXmlRootElement(localName = "report")
public class WeatherResponse {
    private final String time;
    private final String code;
    private final Object coords;
    private final String message;
    private final String rating;

    public WeatherResponse(@JacksonXmlProperty(localName = "time") final String time,
        @JacksonXmlProperty(localName = "code") final String code,
        @JacksonXmlProperty(localName = "coords") final Object coords,
        @JacksonXmlProperty(localName = "message") final String message,
        @JacksonXmlProperty(localName = "varX-Rating") final String rating) {
        this.time = time;
        this.code = code;
        this.coords = coords;
        this.message = message;
        this.rating = rating;
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
