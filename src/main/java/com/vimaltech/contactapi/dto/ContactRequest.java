package com.vimaltech.contactapi.dto;

import com.vimaltech.contactapi.enums.InquiryType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactRequest(

        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotNull(message = "Subject is required")
        InquiryType subject,

        @NotBlank(message = "Message is required")
        @Size(min = 10, max = 1000,
                message = "Message must be between 10 and 1000 characters")
        String message
) {
}