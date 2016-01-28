package me.qyh.dao;

import java.util.List;

import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.TemporaryBlog;

public interface TemporaryBlogDao extends BaseDao<Blog, Integer> {

	List<TemporaryBlog> selectByBlog(Blog blog);

	void deleteByBlog(Blog blog);

}
