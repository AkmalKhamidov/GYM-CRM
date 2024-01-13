package com.epamlearning.controllers.exception;

import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.epamlearning.controllers")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthenticated.class)
    public ResponseEntity<ErrorResponse> handleNotAuthenticatedException(NotAuthenticated ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }



}
