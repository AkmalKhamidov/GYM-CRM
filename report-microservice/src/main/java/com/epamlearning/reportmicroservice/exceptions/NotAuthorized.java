package com.epamlearning.reportmicroservice.exceptions;

public class NotAuthorized extends RuntimeException{
    public NotAuthorized(String message) {
        super(message);
    }
}
