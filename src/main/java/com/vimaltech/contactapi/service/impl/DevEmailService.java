package com.vimaltech.contactapi.service.impl;

import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.entity.EmailLog;
import com.vimaltech.contactapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@Slf4j
public class DevEmailService implements EmailService {

    @Override
    public void sendEmail(EmailRequest request) {

        log.info("""
                
                ==============================
                DEV EMAIL SIMULATION
                ==============================
                TO: {}
                SUBJECT: {}
                
                BODY:
                {}
                ==============================
                """,
                request.getTo(),
                request.getSubject(),
                request.getBody()
        );
    }

    @Override
    public void retryEmail(EmailLog emailLog) {

        log.info("""
            
            ==============================
            DEV EMAIL RETRY
            ==============================
            TO: {}
            SUBJECT: {}
            
            BODY:
            {}
            
            RETRY COUNT: {}
            ==============================
            """,
                emailLog.getToEmail(),
                emailLog.getSubject(),
                emailLog.getBody(),
                emailLog.getRetryCount()
        );
    }
}