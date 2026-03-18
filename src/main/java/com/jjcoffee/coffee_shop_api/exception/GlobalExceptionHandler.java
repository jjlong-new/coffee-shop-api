package com.jjcoffee.coffee_shop_api.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Global exception handler to handle specific exceptions and return appropriate HTTP responses
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException and return a 404 status with the exception message
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception){
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    // Handle HttpMessageNotReadableException and return a 400 status with a custom message if the error is related to Size enum, otherwise return the original exception message
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){

        if (exception.getMessage().contains("Size")) {
            return ResponseEntity.status(400).body("Invalid size. Only allowed values are: SMALL, MEDIUM, LARGE");
        }

        return ResponseEntity.status(400).body("Invalid request body: " + exception.getMessage());
    }

    // Handle MethodArgumentNotValidException and return a 400 status with a list of field errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        
        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        return ResponseEntity.badRequest().body(
            Map.of(
                "errorMessage", "Validation failed",
                "fieldErrors", errors
            )
        );
    }

}
