package com.vimaltech.contactapi.dto.admin;

import java.time.LocalDateTime;

public record RetryOperationResponse(

        boolean success,

        String message,

        Long emailId,

        LocalDateTime timestamp
) {
}