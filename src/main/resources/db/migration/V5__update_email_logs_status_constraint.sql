ALTER TABLE email_logs
DROP CONSTRAINT IF EXISTS email_logs_status_check;

ALTER TABLE email_logs
ADD CONSTRAINT email_logs_status_check
CHECK (
    status IN (
        'PENDING',
        'SENT',
        'FAILED',
        'IN_PROGRESS',
        'FAILED_PERMANENT'
    )
);