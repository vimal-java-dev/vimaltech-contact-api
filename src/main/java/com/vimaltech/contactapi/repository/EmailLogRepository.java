package com.vimaltech.contactapi.repository;

import com.vimaltech.contactapi.entity.EmailLog;
import com.vimaltech.contactapi.enums.EmailStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailLogRepository
        extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findTop10ByStatusOrderByCreatedAtAsc(
            EmailStatus status
    );

    Page<EmailLog> findByStatus(
            EmailStatus status,
            Pageable pageable
    );

    List<EmailLog> findByStatus(
            EmailStatus status
    );

    long countByStatus(
            EmailStatus status
    );
}