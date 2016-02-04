package me.qyh.service;

import java.util.List;

import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.BlogComment;
import me.qyh.entity.blog.TemporaryBlog;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.pageparam.Page;

public interface BlogService {

	void insertBlog(Blog blog) throws LogicException;

	void deleteBlog(Integer id) throws LogicException;

	BlogCategory insertOrUpdateBlogCategory(BlogCategory category) throws LogicException;

	List<BlogCategory> findBlogCategorys(Space space);

	void deleteBlogCategory(Integer id) throws LogicException;

	Page<Blog> findBlogs(BlogPageParam param);

	void updateHits(Integer blog, int hits) throws LogicException;

	void deleteBlogLogic(Integer id) throws LogicException;

	void recover(Integer id) throws LogicException;

	void updateBlog(Blog blog) throws LogicException;

	void insertOrUpdateTemporaryBlog(Blog blog) throws LogicException;

	TemporaryBlog getTemporaryBlog(Blog blog);

	Blog getTemporaryBlog(Integer id) throws LogicException;

	void handleTemporaryBlog(Blog blog) throws LogicException;

	Blog getBlog(Integer id) throws LogicException;
	
	List<Blog> findAroundBlogs(Integer id, BlogPageParam param);

	Page<BlogComment> findComments(CommentPageParam param) throws LogicException;

	BlogComment insertComment(BlogComment comment) throws LogicException;

	void deleteComment(Integer id) throws LogicException;
	
	String preview(String content);
}
