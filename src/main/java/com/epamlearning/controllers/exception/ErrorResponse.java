package com.epamlearning.controllers.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Long timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
