package com.vimaltech.contactapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailRequest {
    private final String to;
    private final String subject;
    private final String body;

    private final String replyTo;
}