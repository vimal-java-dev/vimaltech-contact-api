package com.vimaltech.contactapi.controller.admin;

import com.vimaltech.contactapi.dto.admin.EmailLogResponse;
import com.vimaltech.contactapi.dto.admin.PagedResponse;
import com.vimaltech.contactapi.dto.admin.RetryOperationResponse;
import com.vimaltech.contactapi.dto.admin.RetryStatisticsResponse;
import com.vimaltech.contactapi.enums.EmailStatus;
import com.vimaltech.contactapi.service.admin.EmailOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/email-retries")
@RequiredArgsConstructor
public class EmailRetryAdminController {

    private final EmailOperationsService emailOperationsService;

    @GetMapping("/stats")
    public ResponseEntity<RetryStatisticsResponse> getStatistics() {

        return ResponseEntity.ok(
                emailOperationsService.getStatistics()
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<EmailLogResponse>> getEmailLogs(

            @RequestParam(required = false)
            EmailStatus status,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<EmailLogResponse> emailPage =
                emailOperationsService.getEmailLogs(
                        status,
                        pageable
                );

        PagedResponse<EmailLogResponse> response =
                new PagedResponse<>(
                        emailPage.getContent(),
                        emailPage.getNumber(),
                        emailPage.getSize(),
                        emailPage.getTotalElements(),
                        emailPage.getTotalPages(),
                        emailPage.isFirst(),
                        emailPage.isLast()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailLogResponse> getEmailLog(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                emailOperationsService.getEmailLog(id)
        );
    }

    @PostMapping("/{id}/retry")
    public ResponseEntity<RetryOperationResponse> retryEmail(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                emailOperationsService.retryEmail(id)
        );
    }

    @PostMapping("/retry-all")
    public ResponseEntity<RetryOperationResponse> retryAllFailedEmails() {

        return ResponseEntity.ok(
                emailOperationsService.retryAllFailedEmails()
        );
    }

    @PostMapping("/{id}/reset")
    public ResponseEntity<RetryOperationResponse> resetPermanentFailure(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                emailOperationsService.resetPermanentFailure(id)
        );
    }
}