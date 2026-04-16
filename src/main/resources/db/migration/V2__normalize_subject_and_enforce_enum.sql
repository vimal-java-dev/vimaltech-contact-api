-- ============================
-- 1. Normalize existing values
-- ============================

UPDATE contact_inquiries
SET subject = UPPER(subject)
WHERE subject IS NOT NULL;

-- ============================
-- 2. Fix known variations
-- ============================

UPDATE contact_inquiries
SET subject = 'BACKEND'
WHERE subject ILIKE 'backend';
UPDATE contact_inquiries
SET subject = 'FULLSTACK'
WHERE subject ILIKE 'fullstack';
UPDATE contact_inquiries
SET subject = 'VPS'
WHERE subject ILIKE 'vps';
UPDATE contact_inquiries
SET subject = 'OTHER'
WHERE subject ILIKE 'other';
UPDATE contact_inquiries
SET subject = 'FRONTEND'
WHERE subject ILIKE 'frontend';

-- ============================
-- 3. Fix NULL values
-- ============================

UPDATE contact_inquiries
SET subject = 'OTHER'
WHERE subject IS NULL;

-- ============================
-- 4. Enforce NOT NULL
-- ============================

ALTER TABLE contact_inquiries
ALTER COLUMN subject SET NOT NULL;

-- ============================
-- 5. ADD CHECK CONSTRAINT (LAST STEP)
-- ============================

ALTER TABLE contact_inquiries
ADD CONSTRAINT chk_subject_valid
CHECK (subject IN ('FRONTEND', 'BACKEND', 'FULLSTACK', 'VPS', 'OTHER'));