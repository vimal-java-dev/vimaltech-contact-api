package com.vimaltech.contactapi.controller;

import com.vimaltech.contactapi.config.AppProperties;
import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Profile({"dev"})
//@Profile({"dev", "local"})
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;
    private final AppProperties appProperties;

    @GetMapping("/email")
    public String testEmail() {

        emailService.sendEmail(
                EmailRequest.builder()
                        .to(appProperties.getEmail()) // 👈 CHANGE THIS
                        .subject("Test Email from VimalTech API")
                        .body("Email service is working successfully 🚀")
                        .build()
        );

        return "Email sent successfully";
    }
}