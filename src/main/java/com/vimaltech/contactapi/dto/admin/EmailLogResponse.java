package com.vimaltech.contactapi.dto.admin;

import com.vimaltech.contactapi.enums.EmailStatus;

import java.time.LocalDateTime;

public record EmailLogResponse(

        Long id,

        String toEmail,

        String subject,

        EmailStatus status,

        int retryCount,

        String lastError,

        LocalDateTime createdAt,

        LocalDateTime lastAttemptAt
) {
}