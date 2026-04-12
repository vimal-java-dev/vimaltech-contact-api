package com.vimaltech.contactapi.service.impl;

import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public SmtpEmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String from
    ) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    @Async("emailExecutor")
    public void sendEmail(EmailRequest request) {
        try {
            log.info("START: Sending email | to={} | thread={}",
                    request.getTo(), Thread.currentThread().getName());
            
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(from); // 🔥 CRITICAL FIX
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());

            // ✅ OPTIONAL but IMPORTANT
            if (request.getReplyTo() != null && !request.getReplyTo().isBlank()) {
                message.setReplyTo(request.getReplyTo().trim());
            }

            mailSender.send(message);

            log.info("SUCCESS: Email sent | to={}", request.getTo());

        } catch (Exception e) {
            // ❗ DO NOT THROW in async
            log.error("ERROR: Email failed | to={}", request.getTo(), e);
        }
    }
}