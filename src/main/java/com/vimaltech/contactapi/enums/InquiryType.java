package com.vimaltech.contactapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum InquiryType {

    FRONTEND("Frontend Development"),
    BACKEND("Java Backend / API Development"),
    FULLSTACK("Full-Stack Web Development"),
    VPS("Deployment / Server Setup"),
    OTHER("General Inquiry / Consultation");

    private final String label;

    InquiryType(String label) {
        this.label = label;
    }

    public static InquiryType[] list() {
        return values();
    }

    // ✅ Case-insensitive parsing (already discussed)
    @JsonCreator
    public static InquiryType from(String value) {
        if (value == null) {
            return null;
        }

        try {
            return InquiryType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid subject value: " + value +
                            ". Allowed values: FRONTEND, BACKEND, FULLSTACK, VPS, OTHER"
            );
        }
    }
}