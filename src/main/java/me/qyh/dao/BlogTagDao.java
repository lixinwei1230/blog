package me.qyh.dao;

import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogTag;

public interface BlogTagDao extends BaseDao<BlogTag, Integer> {

	void deleteByBlog(Blog blog);
	
}
