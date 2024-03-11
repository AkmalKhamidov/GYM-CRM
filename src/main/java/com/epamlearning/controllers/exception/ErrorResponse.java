package com.epamlearning.controllers.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Long timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String message, Long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
