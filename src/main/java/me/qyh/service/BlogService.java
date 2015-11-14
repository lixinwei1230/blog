package me.qyh.service;

import java.util.List;

import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.TemporaryBlog;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;

public interface BlogService {

	void insertBlog(Blog blog) throws LogicException;

	void deleteBlog(Integer id) throws LogicException;

	BlogCategory insertOrUpdateBlogCategory(BlogCategory category) throws LogicException;

	List<BlogCategory> findBlogCategorys(Space space);

	void deleteBlogCategory(Integer id) throws LogicException;

	Page<Blog> findBlogs(BlogPageParam param);

	void updateHits(Integer blog, int hits);

	void deleteBlogLogic(Integer id) throws LogicException;

	void recover(Integer id) throws LogicException;

	void updateBlog(Blog blog) throws LogicException;

	void insertOrUpdateTemporaryBlog(Blog blog);

	TemporaryBlog getTemporaryBlog(Blog blog);

	Blog getTemporaryBlog(Integer id) throws LogicException;

	void handleTemporaryBlog(Blog blog) throws LogicException;

	Blog getBlog(Integer id) throws LogicException;

}
