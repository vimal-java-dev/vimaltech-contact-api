package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.config.AppProperties;
import com.vimaltech.contactapi.dto.ContactRequest;
import com.vimaltech.contactapi.dto.EmailRequest;
import com.vimaltech.contactapi.entity.ContactInquiry;
import com.vimaltech.contactapi.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void processContact(ContactRequest request) {

        // ✅ 1. Save to DB (unchanged behavior)
        ContactInquiry inquiry = ContactInquiry.builder()
                .name(request.name())
                .email(request.email())
                .subject(request.subject())
                .message(request.message())
                .build();

        contactRepository.save(inquiry);

        log.info("Contact saved | email={}", request.email());

        // ✅ 2. Send emails {(non-blocking for business logic), try-catch removed}
        // ✅ Fire async emails (NO try-catch)
        sendEmails(request);
    }

    // ✅ NEW METHOD (clean separation)
    private void sendEmails(ContactRequest request) {

        // 📩 Admin Email
        EmailRequest adminEmail = EmailRequest.builder()
                .to(appProperties.getEmail())
                .subject("New Contact Inquiry: " +
                        (request.subject() != null ? request.subject() : "No Subject"))
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
                        request.subject(),
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

                Thank you for reaching out. We have received your message and will respond shortly.

                Best regards,
                VimalTech Team
                """.formatted(request.name()))
                .build();

        emailService.sendEmail(userEmail);
    }

    public List<ContactInquiry> getAllContacts() {
        return contactRepository.findAll();
    }
}