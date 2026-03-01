package com.vimaltech.contactapi.controller;

import com.vimaltech.contactapi.dto.ApiResponse;
import com.vimaltech.contactapi.dto.ContactRequest;
import com.vimaltech.contactapi.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ApiResponse> submitContact(
            @Valid @RequestBody ContactRequest request) {

        contactService.processContact(request);

        ApiResponse response = new ApiResponse(
                true,
                "Message received successfully.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}