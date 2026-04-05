package com.vimaltech.contactapi.exception;

import com.vimaltech.contactapi.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Validation error");

        // Optional but useful
        log.warn("Validation failed: {}", errorMessage);

        ApiResponse response = new ApiResponse(
                false,
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {

        // 🔥 CRITICAL LINE (THIS WAS MISSING)
        log.error("Unhandled exception occurred", ex);

        ApiResponse response = new ApiResponse(
                false,
                "Something went wrong. Please try again later.",
                LocalDateTime.now()
        );

        return ResponseEntity.internalServerError().body(response);
    }
}