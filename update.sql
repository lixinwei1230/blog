UPDATE blog_user SET user_password = '$2a$10$eJ8pM6Zl9tltoO/aHu9FFuUI6RIBoJmC8q740jajSj1X.Wt0JDowq' WHERE user_name = 'test';
INSERT INTO user_role(user_id,role_id)
VALUES((SELECT id FROM blog_user WHERE user_name = 'test'),(SELECT id FROM role WHERE role_name = 'ROLE_SUPERVISOR'));
ALTER TABLE blog ADD COLUMN editor INT NOT NULL DEFAULT 0;
ALTER TABLE temporary_blog ADD COLUMN editor INT NOT NULL DEFAULT 0;
ALTER TABLE blog ADD COLUMN display MEDIUMTEXT NOT NULL;

ALTER TABLE blog_file ADD COLUMN width INT;
ALTER TABLE blog_file ADD COLUMN height INT;
UPDATE blog_file SET file_store = 1;
UPDATE blog_user SET avatar = NULL;
