package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.entity.EmailLog;

public interface EmailService {
    void sendEmail(EmailRequest request);

    void retryEmail(EmailLog emailLog);
}