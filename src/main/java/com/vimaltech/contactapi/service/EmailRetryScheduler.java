package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.entity.EmailLog;
import com.vimaltech.contactapi.enums.EmailStatus;
import com.vimaltech.contactapi.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailRetryScheduler {

    private static final int MAX_RETRIES = 3;

    private final EmailLogRepository emailLogRepository;
    private final EmailService emailService;

    @Scheduled(fixedDelay = 60000)
    public void retryFailedEmails() {

        List<EmailLog> failedEmails =
                emailLogRepository
                        .findTop10ByStatusOrderByCreatedAtAsc(
                                EmailStatus.FAILED
                        );

        if (failedEmails.isEmpty()) {
            return;
        }

        log.info(
                "Retry scheduler found {} failed emails",
                failedEmails.size()
        );

        for (EmailLog emailLog : failedEmails) {

            /*
             * Prevent infinite retry polling
             */
            if (emailLog.getRetryCount() >= MAX_RETRIES) {

                emailLog.setStatus(
                        EmailStatus.FAILED_PERMANENT
                );

                emailLog.setLastError(
                        "Retry limit exhausted"
                );

                emailLogRepository.save(emailLog);

                log.error(
                        "Email permanently failed after {} retries. Email ID={}",
                        emailLog.getRetryCount(),
                        emailLog.getId()
                );

                continue;
            }

            /*
             * Prevent retry spam within 1 minute
             */
            if (emailLog.getLastAttemptAt() != null
                    && emailLog.getLastAttemptAt()
                    .isAfter(LocalDateTime.now().minusMinutes(1))) {

                continue;
            }

            log.warn(
                    "Retrying email attempt {}/{} for email id={}",
                    emailLog.getRetryCount() + 1,
                    MAX_RETRIES,
                    emailLog.getId()
            );

            emailService.retryEmail(emailLog);
        }
    }
}