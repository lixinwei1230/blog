ALTER TABLE blog ADD COLUMN recommend BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE blog_file ADD COLUMN cover INT ;
ALTER TABLE blog_file ADD FOREIGN KEY(cover) REFERENCES blog_file(id);
ALTER TABLE blog_file ADD COLUMN is_cover BOOLEAN NOT NULL DEFAULT FALSE ;

RENAME TABLE web_tag to tag;
DROP TABLE user_tag_count;
DROP TABLE web_tag_count;
DROP TABLE user_tag;
CREATE TABLE user_tag(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,user_id INT NOT NULL,
tag_id INT NOT NULL ,
FOREIGN KEY(user_id) REFERENCES blog_user(id),
FOREIGN KEY(tag_id) REFERENCES tag(id));

CREATE TABLE login_info(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,user_id INT NOT NULL,
login_date DATETIME NOT NULL , remote_address VARCHAR(100) NOT NULL,
FOREIGN KEY(user_id) REFERENCES blog_user(id));

ALTER TABLE widget_config ADD COLUMN scope INT NOT NULL DEFAULT 0;
ALTER TABLE widget_blogwidgetconfig ADD COLUMN scope INT NOT NULL DEFAULT 0;

ALTER TABLE oauth_user DROP FOREIGN KEY oauth_user_ibfk_1;
ALTER TABLE oauth_user DROP INDEX user_id;
ALTER TABLE oauth_user ADD FOREIGN KEY(user_id) REFERENCES blog_user(id);


INSERT INTO user_role(user_id,role_id)
VALUES((SELECT id FROM blog_user WHERE user_name = 'test'),(SELECT id FROM role WHERE role_name = 'ROLE_SUPERVISOR'))

UPDATE message_send SET message_type = 2 WHERE message_type = 3;

UPDATE blog_file SET file_store = 0 WHERE file_store = -1;