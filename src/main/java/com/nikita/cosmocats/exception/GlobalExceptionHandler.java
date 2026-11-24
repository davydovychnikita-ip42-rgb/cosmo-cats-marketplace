package com.nikita.cosmocats.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String detailMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "Field '" + error.getField() + "' " + error.getDefaultMessage())
                .findFirst().orElse("Validation failed");

        Map<String, Object> body = new HashMap<>();
        body.put("type", "https://cosmo-cats.market/errors/validation");
        body.put("title", "Validation failed");
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("detail", detailMessage);
        body.put("instance", request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
