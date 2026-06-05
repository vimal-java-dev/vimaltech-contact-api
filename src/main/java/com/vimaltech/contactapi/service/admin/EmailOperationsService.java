package com.vimaltech.contactapi.service.admin;

import com.vimaltech.contactapi.dto.admin.EmailLogResponse;
import com.vimaltech.contactapi.dto.admin.RetryOperationResponse;
import com.vimaltech.contactapi.dto.admin.RetryStatisticsResponse;
import com.vimaltech.contactapi.enums.EmailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailOperationsService {

    RetryStatisticsResponse getStatistics();

    Page<EmailLogResponse> getEmailLogs(
            EmailStatus status,
            Pageable pageable
    );

    EmailLogResponse getEmailLog(Long id);

    RetryOperationResponse retryEmail(Long id);

    RetryOperationResponse retryAllFailedEmails();

    RetryOperationResponse resetPermanentFailure(Long id);
}