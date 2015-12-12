package me.qyh.dao;

import java.util.List;

import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogTag;
import me.qyh.entity.tag.Tag;

public interface BlogTagDao extends BaseDao<BlogTag, Integer> {

	void deleteByBlog(Blog blog);
	
	List<Tag> selectByBlog(Blog blog);

}
