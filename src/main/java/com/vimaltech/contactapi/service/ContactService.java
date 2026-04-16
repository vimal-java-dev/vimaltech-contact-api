package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.config.AppProperties;
import com.vimaltech.contactapi.dto.ContactRequest;
import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.dto.InquiryOption;
import com.vimaltech.contactapi.entity.ContactInquiry;
import com.vimaltech.contactapi.enums.InquiryType;
import com.vimaltech.contactapi.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;
    private final EmailService emailService;
    private final AppProperties appProperties;

    public ContactService(ContactRepository repo, EmailService emailService, AppProperties appProperties) {
        this.contactRepository = repo;
        this.emailService = emailService;
        this.appProperties = appProperties;
    }

    public List<InquiryOption> getInquiryOptions() {
        return Arrays.stream(InquiryType.values())
                .map(type -> new InquiryOption(
                        type.name(),        // BACKEND
                        type.getLabel()     // Java Backend...
                ))
                .toList();
    }

    public void processContact(ContactRequest request) {

        // ✅ Save to DB
        ContactInquiry inquiry = ContactInquiry.builder()
                .name(request.name())
                .email(request.email())
                .subject(request.subject())
                .message(request.message())
                .build();

        contactRepository.save(inquiry);

        log.info("Contact saved | email={} | subject={}", request.email(), request.subject());

        // ✅ Send async emails
        sendEmails(request);
    }

    private void sendEmails(ContactRequest request) {

        String subjectLabel = request.subject().getLabel(); // ✅ human-readable

        // 📩 Admin Email
        EmailRequest adminEmail = EmailRequest.builder()
                .to(appProperties.getEmail())
                .subject("New Contact Inquiry: " + subjectLabel)
                .body("""
            New contact inquiry received:

            Name: %s
            Email: %s
            Subject: %s

            Message:
            %s
            """.formatted(
                        request.name(),
                        request.email(),
                        subjectLabel,
                        request.message()
                ))
                .replyTo(request.email())
                .build();

        emailService.sendEmail(adminEmail);

        // 📩 User Confirmation Email
        EmailRequest userEmail = EmailRequest.builder()
                .to(request.email())
                .subject("Thanks for contacting VimalTech")
                .body("""
            Hi %s,

            Thank you for reaching out regarding "%s".

            We have received your message and will respond shortly.

            Best regards,
            VimalTech Team
            """.formatted(
                        request.name(),
                        subjectLabel
                ))
                .build();

        emailService.sendEmail(userEmail);
    }

    public List<ContactInquiry> getAllContacts() {
        return contactRepository.findAll();
    }
}