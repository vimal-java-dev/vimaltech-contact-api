package com.vimaltech.contactapi.service.impl;

import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.entity.EmailLog;
import com.vimaltech.contactapi.enums.EmailStatus;
import com.vimaltech.contactapi.repository.EmailLogRepository;
import com.vimaltech.contactapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile("prod")
@Slf4j
@ConditionalOnProperty(name = "spring.mail.host")
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final String from;
    private final EmailLogRepository emailLogRepository;
    private static final int MAX_RETRIES = 3;

    public SmtpEmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String from,
            EmailLogRepository emailLogRepository
    ) {
        this.mailSender = mailSender;
        this.from = from;
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    @Async("emailExecutor")
    public void sendEmail(EmailRequest request) {

        EmailLog emailLog = EmailLog.builder()
                .toEmail(request.getTo())
                .subject(request.getSubject())
                .body(request.getBody())
                .status(EmailStatus.PENDING)
                .retryCount(0)
                .build();

        emailLog = emailLogRepository.save(emailLog);

        processEmailSend(emailLog, request);
    }

    private void processEmailSend(
            EmailLog emailLog,
            EmailRequest request
    ) {

        try {

            log.info("START: Sending email | to={} | thread={}",
                    request.getTo(),
                    Thread.currentThread().getName());

            emailLog.setStatus(EmailStatus.IN_PROGRESS);
            emailLog.setLastAttemptAt(LocalDateTime.now());

            emailLogRepository.save(emailLog);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from);
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            if (request.getReplyTo() != null
                    && !request.getReplyTo().isBlank()) {

                message.setReplyTo(request.getReplyTo().trim());
            }

            mailSender.send(message);

            emailLog.setStatus(EmailStatus.SENT);

            log.info("SUCCESS: Email sent | to={}",
                    request.getTo());

        } catch (Exception e) {

            int updatedRetryCount =
                    emailLog.getRetryCount() + 1;

            emailLog.setRetryCount(updatedRetryCount);

            emailLog.setLastError(e.getMessage());

            /*
             * Move to terminal failure state
             * after max retries exhausted
             */
            if (updatedRetryCount >= MAX_RETRIES) {

                emailLog.setStatus(
                        EmailStatus.FAILED_PERMANENT
                );

                log.error(
                        "PERMANENT FAILURE: Email exhausted retries | to={}",
                        request.getTo(),
                        e
                );

            } else {

                emailLog.setStatus(EmailStatus.FAILED);

                log.warn(
                        "RETRY FAILURE: Email send failed | retryCount={} | to={}",
                        updatedRetryCount,
                        request.getTo(),
                        e
                );
            }
        }

        emailLog.setLastAttemptAt(LocalDateTime.now());

        emailLogRepository.save(emailLog);
    }

    @Override
    @Async("emailExecutor")
    public void retryEmail(EmailLog emailLog) {

        EmailRequest request = EmailRequest.builder()
                .to(emailLog.getToEmail())
                .subject(emailLog.getSubject())
                .body(emailLog.getBody())
                .build();

        processEmailSend(emailLog, request);
    }
}