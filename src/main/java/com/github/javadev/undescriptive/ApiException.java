package com.github.javadev.undescriptive;

import com.github.javadev.undescriptive.protocol.response.ErrorResponse;

import java.net.URI;

public class ApiException extends RuntimeException {
    private final URI uri;
    private final ErrorResponse errorResponse;

    public ApiException(final URI uri, final ErrorResponse errorResponse) {
        super(uri + " " + errorResponse.getError().toString());
        this.uri = uri;
        this.errorResponse = errorResponse;
    }

    public URI getUri() {
        return uri;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    @Override
    public String toString() {
        return "ApiException: " + uri + " " + errorResponse.getError().toString();
    }
}
