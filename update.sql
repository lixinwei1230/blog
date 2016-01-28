UPDATE blog_user SET user_password = '$2a$10$eJ8pM6Zl9tltoO/aHu9FFuUI6RIBoJmC8q740jajSj1X.Wt0JDowq' WHERE user_name = 'test';
INSERT INTO user_role(user_id,role_id)
VALUES((SELECT id FROM blog_user WHERE user_name = 'test'),(SELECT id FROM role WHERE role_name = 'ROLE_SUPERVISOR'))