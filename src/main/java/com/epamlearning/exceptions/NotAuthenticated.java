package com.epamlearning.exceptions;

public class NotAuthenticated extends RuntimeException{
    public NotAuthenticated(String message) {
        super(message);
    }
}
