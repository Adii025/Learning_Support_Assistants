-- Run this ONCE after deploying the fixed backend, if you already registered
-- Parent or LSA accounts before the fix (their "users" row exists, but the
-- matching "parents" / "lsa_profiles" row was never created).
--
-- Usage: psql -U <youruser> -d <yourdb> -f backfill_existing_accounts.sql

INSERT INTO parents (user_id)
SELECT u.id
FROM users u
WHERE u.role = 'PARENT'
  AND NOT EXISTS (SELECT 1 FROM parents p WHERE p.user_id = u.id);

INSERT INTO lsa_profiles (user_id, active)
SELECT u.id, TRUE
FROM users u
WHERE u.role = 'LSA'
  AND NOT EXISTS (SELECT 1 FROM lsa_profiles l WHERE l.user_id = u.id);
