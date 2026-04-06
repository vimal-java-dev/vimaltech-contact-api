package com.vimaltech.contactapi.service;

import com.vimaltech.contactapi.dto.ContactRequest;
import com.vimaltech.contactapi.entity.ContactInquiry;
import com.vimaltech.contactapi.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public void processContact(ContactRequest request) {

        ContactInquiry inquiry = ContactInquiry.builder()
                .name(request.name())
                .email(request.email())
                .subject(request.subject())
                .message(request.message())
                .createdAt(LocalDateTime.now())
                .build();

        contactRepository.save(inquiry);
    }

    public List<ContactInquiry> getAllContacts() {
        return contactRepository.findAll();
    }
}