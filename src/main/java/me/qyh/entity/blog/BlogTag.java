package me.qyh.entity.blog;

import me.qyh.entity.Id;
import me.qyh.entity.tag.Tag;

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
	private Tag tag;

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public BlogTag(Blog blog, Tag tag) {
		this.blog = blog;
		this.tag = tag;
	}

	public BlogTag() {
	}

}
