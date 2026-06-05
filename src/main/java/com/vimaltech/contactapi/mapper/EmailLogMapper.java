package com.vimaltech.contactapi.mapper;

import com.vimaltech.contactapi.dto.admin.EmailLogResponse;
import com.vimaltech.contactapi.entity.EmailLog;

public final class EmailLogMapper {

    private EmailLogMapper() {
    }

    public static EmailLogResponse toResponse(
            EmailLog emailLog
    ) {

        return new EmailLogResponse(
                emailLog.getId(),
                emailLog.getToEmail(),
                emailLog.getSubject(),
                emailLog.getStatus(),
                emailLog.getRetryCount(),
                emailLog.getLastError(),
                emailLog.getCreatedAt(),
                emailLog.getLastAttemptAt()
        );
    }
}