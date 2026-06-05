package com.vimaltech.contactapi.exception;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(Long id) {
        super("Email log not found with id: " + id);
    }
}