package com.vimaltech.contactapi.service.admin;

import com.vimaltech.contactapi.dto.admin.EmailLogResponse;
import com.vimaltech.contactapi.dto.admin.RetryOperationResponse;
import com.vimaltech.contactapi.dto.admin.RetryStatisticsResponse;
import com.vimaltech.contactapi.entity.EmailLog;
import com.vimaltech.contactapi.enums.EmailStatus;
import com.vimaltech.contactapi.exception.EmailNotFoundException;
import com.vimaltech.contactapi.mapper.EmailLogMapper;
import com.vimaltech.contactapi.repository.EmailLogRepository;
import com.vimaltech.contactapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailOperationsServiceImpl
        implements EmailOperationsService {

    private final EmailLogRepository emailLogRepository;
    private final EmailService emailService;

    @Override
    public RetryStatisticsResponse getStatistics() {

        return new RetryStatisticsResponse(
                emailLogRepository.countByStatus(EmailStatus.PENDING),
                emailLogRepository.countByStatus(EmailStatus.IN_PROGRESS),
                emailLogRepository.countByStatus(EmailStatus.SENT),
                emailLogRepository.countByStatus(EmailStatus.FAILED),
                emailLogRepository.countByStatus(EmailStatus.FAILED_PERMANENT)
        );
    }

    @Override
    public Page<EmailLogResponse> getEmailLogs(
            EmailStatus status,
            Pageable pageable
    ) {

        if (status == null) {

            return emailLogRepository.findAll(pageable)
                    .map(EmailLogMapper::toResponse);
        }

        return emailLogRepository.findByStatus(
                        status,
                        pageable
                )
                .map(EmailLogMapper::toResponse);
    }

    @Override
    public EmailLogResponse getEmailLog(Long id) {

        EmailLog emailLog = emailLogRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmailNotFoundException(id));

        return EmailLogMapper.toResponse(emailLog);
    }

    @Override
    public RetryOperationResponse retryEmail(Long id) {

        EmailLog emailLog = emailLogRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmailNotFoundException(id));

        // Prevent duplicate resend
        if (emailLog.getStatus() == EmailStatus.SENT) {

            return new RetryOperationResponse(
                    false,
                    "Email already sent",
                    id,
                    LocalDateTime.now()
            );
        }

        // Prevent retry while already processing
        if (emailLog.getStatus() == EmailStatus.IN_PROGRESS) {

            return new RetryOperationResponse(
                    false,
                    "Email is currently being processed",
                    id,
                    LocalDateTime.now()
            );
        }

        emailService.retryEmail(emailLog);

        return new RetryOperationResponse(
                true,
                "Email retry triggered successfully",
                id,
                LocalDateTime.now()
        );
    }

    @Override
    public RetryOperationResponse retryAllFailedEmails() {

        List<EmailLog> failedEmails =
                emailLogRepository.findByStatus(
                        EmailStatus.FAILED
                );

        failedEmails.forEach(
                emailService::retryEmail
        );

        return new RetryOperationResponse(
                true,
                "Retry triggered for "
                        + failedEmails.size()
                        + " failed email(s)",
                null,
                LocalDateTime.now()
        );
    }

    @Override
    public RetryOperationResponse resetPermanentFailure(
            Long id
    ) {

        EmailLog emailLog = emailLogRepository
                .findById(id)
                .orElseThrow(() ->
                        new EmailNotFoundException(id));

        emailLog.setRetryCount(0);
        emailLog.setLastError(null);
        emailLog.setStatus(EmailStatus.FAILED);

        emailLogRepository.save(emailLog);

        return new RetryOperationResponse(
                true,
                "Email retry state reset successfully",
                id,
                LocalDateTime.now()
        );
    }
}