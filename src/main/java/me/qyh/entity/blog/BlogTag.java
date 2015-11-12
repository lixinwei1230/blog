package me.qyh.entity.blog;

import me.qyh.entity.Id;
import me.qyh.entity.tag.WebTag;

/**
 * 博客 标签关联
 * 
 * @author mhlx
 *
 */
public class BlogTag extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Blog blog;
	private WebTag tag;

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public WebTag getTag() {
		return tag;
	}

	public void setWebTag(WebTag tag) {
		this.tag = tag;
	}

	public BlogTag(Blog blog, WebTag tag) {
		this.blog = blog;
		this.tag = tag;
	}

	public BlogTag() {
	}

}
