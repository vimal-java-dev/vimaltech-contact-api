package com.vimaltech.contactapi.service.impl;

import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class NoOpEmailService implements EmailService {

    @Override
    public void sendEmail(EmailRequest request) {
        log.warn("Email disabled (NoOp) | to={}", request.getTo());
    }
}