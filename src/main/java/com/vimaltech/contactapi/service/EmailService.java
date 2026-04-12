package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.dto.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);
}