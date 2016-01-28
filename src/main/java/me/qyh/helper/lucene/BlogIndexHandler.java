package me.qyh.helper.lucene;

import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;

public interface BlogIndexHandler {

	public void rebuildBlogIndex(Blog ... blogs);

	public void removeBlogIndex(Blog ... blogs);

	public Page<Blog> search(BlogPageParam param);

}
