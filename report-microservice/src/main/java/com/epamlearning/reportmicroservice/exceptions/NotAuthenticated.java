package com.epamlearning.reportmicroservice.exceptions;

public class NotAuthenticated extends RuntimeException{
    public NotAuthenticated(String message) {
        super(message);
    }
}
