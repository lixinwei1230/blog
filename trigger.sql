DELIMITER //
CREATE TRIGGER file_delete
  BEFORE DELETE ON blog_file
FOR EACH ROW
BEGIN
  UPDATE blog_user SET avatar = NULL WHERE avatar = old.id;
END;
//