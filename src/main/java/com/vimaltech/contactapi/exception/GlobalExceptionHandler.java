package com.vimaltech.contactapi.exception;

import com.vimaltech.contactapi.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ✅ 1. Bean validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Validation error");

        log.warn("Validation failed: {}", errorMessage);

        ApiResponse response = new ApiResponse(
                false,
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    // ✅ 2. JSON parsing / ENUM errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleJsonParseError(
            HttpMessageNotReadableException ex) {

        log.warn(
                "Invalid request format or enum value: {}",
                ex.getMessage()
        );

        String message = "Invalid request format";

        Throwable root = ex.getCause();

        if (root instanceof IllegalArgumentException
                && root.getMessage() != null
                && root.getMessage().contains("Invalid subject value")) {

            message = root.getMessage();
        }

        ApiResponse response = new ApiResponse(
                false,
                message,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    // ✅ 3. Email log not found
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEmailNotFoundException(
            EmailNotFoundException ex) {

        log.warn("Email log not found: {}", ex.getMessage());

        ApiResponse response = new ApiResponse(
                false,
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // ✅ 4. Catch-all (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(
            Exception ex) {

        log.error("Unhandled exception occurred", ex);

        ApiResponse response = new ApiResponse(
                false,
                "Something went wrong. Please try again later.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
        ).body(response);
    }
}