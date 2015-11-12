alter table blog add column recommend boolean not null default false;
ALTER TABLE blog_file ADD COLUMN cover INT ;
ALTER TABLE blog_file ADD FOREIGN KEY(cover) REFERENCES blog_file(id);
ALTER TABLE blog_file ADD COLUMN is_cover BOOLEAN NOT NULL DEFAULT FALSE ;