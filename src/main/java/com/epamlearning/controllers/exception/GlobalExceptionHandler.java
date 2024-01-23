package com.epamlearning.controllers.exception;

import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice("com.epamlearning.controllers")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logExceptionDetails(request, ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthenticated.class)
    public ResponseEntity<ErrorResponse> handleNotAuthenticatedException(NotAuthenticated ex, WebRequest request) {
        logExceptionDetails(request, ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logExceptionDetails(request, ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Validation error(s): ");

        result.getFieldErrors().forEach(fieldError -> {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        });
        logExceptionDetails(request, new Exception("MethodArgumentNotValidException" + errorMessage));
        return new ResponseEntity<>(new ErrorResponse(errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    private void logExceptionDetails(WebRequest request, Exception ex) {
        logger.error("Exception occurred for URL: {}", request.getDescription(false));
        logger.error("Exception message: {}", ex.getMessage());
    }

}
