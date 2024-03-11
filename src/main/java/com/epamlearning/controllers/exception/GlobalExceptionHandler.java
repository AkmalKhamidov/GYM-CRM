package com.epamlearning.controllers.exception;

import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotAuthorized;
import com.epamlearning.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.io.IOException;

@RestControllerAdvice("com.epamlearning.controllers")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger("logging");

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

    @ExceptionHandler(NotAuthorized.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedException(NotAuthorized ex, WebRequest request) {
        logExceptionDetails(request, ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }


//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
//        logExceptionDetails(request, ex);
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Validation error(s): ");

        result.getFieldErrors().forEach(fieldError -> errorMessage.append(fieldError.getDefaultMessage()).append("; "));
        logExceptionDetails(request, new Exception("MethodArgumentNotValidException" + errorMessage));
        return new ResponseEntity<>(new ErrorResponse(errorMessage.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HandlerMethodValidationException ex, WebRequest request) {
        String errorMessage = "Validation error(s): " + ex.getMessage();
        logExceptionDetails(request, new Exception("ConstraintViolationException: " + errorMessage));
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(FeignException ex, WebRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        String errorMessage;
        long timestamp;
        try {
            root = mapper.readTree(ex.contentUTF8());
            errorMessage = root.get("message").asText();
            timestamp = root.get("timestamp").asLong();
        } catch (IOException e) {
            // Handle JSON parsing exception
            errorMessage = "Error parsing FeignException message";
            timestamp = System.currentTimeMillis();
        }
        logExceptionDetails(request, new Exception("FeignException: " + ex.getMessage()));
        return new ResponseEntity<>(new ErrorResponse(errorMessage, timestamp), HttpStatusCode.valueOf(ex.status()));
    }

    private void logExceptionDetails(WebRequest request, Exception ex) {
        logger.error("Transaction ID: {}", request.getAttribute("transactionId", WebRequest.SCOPE_REQUEST));
        logger.error("Exception occurred for URL: {}", request.getDescription(true));
        logger.error("Exception message: {}", ex.getMessage());
    }

}
