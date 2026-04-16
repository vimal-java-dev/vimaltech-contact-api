-- ============================
-- 1. Normalize case
-- ============================

UPDATE contact_inquiries
SET subject = UPPER(subject)
WHERE subject IS NOT NULL;

-- ============================
-- 2. Map known values
-- ============================

UPDATE contact_inquiries SET subject = 'BACKEND'   WHERE subject LIKE '%BACKEND%';
UPDATE contact_inquiries SET subject = 'FULLSTACK' WHERE subject LIKE '%FULLSTACK%';
UPDATE contact_inquiries SET subject = 'VPS'       WHERE subject LIKE '%VPS%';
UPDATE contact_inquiries SET subject = 'FRONTEND'  WHERE subject LIKE '%FRONTEND%';
UPDATE contact_inquiries SET subject = 'OTHER'     WHERE subject LIKE '%OTHER%';

-- ============================
-- 3. Handle NULL
-- ============================

UPDATE contact_inquiries
SET subject = 'OTHER'
WHERE subject IS NULL;

-- ============================
-- 4. 🔥 CRITICAL: Catch ALL remaining invalid values
-- ============================

UPDATE contact_inquiries
SET subject = 'OTHER'
WHERE subject NOT IN ('FRONTEND', 'BACKEND', 'FULLSTACK', 'VPS', 'OTHER');

-- ============================
-- 5. Enforce NOT NULL
-- ============================

ALTER TABLE contact_inquiries
ALTER COLUMN subject SET NOT NULL;

-- ============================
-- 6. Add CHECK constraint
-- ============================

ALTER TABLE contact_inquiries
ADD CONSTRAINT chk_subject_valid
CHECK (subject IN ('FRONTEND', 'BACKEND', 'FULLSTACK', 'VPS', 'OTHER'));