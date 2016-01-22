package me.qyh.helper.lucene;

import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;

public interface BlogIndexHandler {

	public void addBlogIndex(Blog blog);

	public void rebuildBlogIndex(Blog blog);

	public void removeBlogIndex(Blog blog);

	public Page<Blog> search(BlogPageParam param);

}
