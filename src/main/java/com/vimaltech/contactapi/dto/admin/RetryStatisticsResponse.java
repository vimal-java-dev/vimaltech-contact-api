package com.vimaltech.contactapi.dto.admin;

public record RetryStatisticsResponse(

        long pending,

        long inProgress,

        long sent,

        long failed,

        long failedPermanent
) {
}