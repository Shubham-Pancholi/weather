-- Version denotes a change in password.
-- Useful to invalidate tokens when password is changed
ALTER TABLE users
ADD COLUMN version INTEGER NOT NULL DEFAULT 0;