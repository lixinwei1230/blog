package me.qyh.pageparam;

import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogComment;
import me.qyh.web.InvalidParamException;

public class CommentPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Blog blog;
	private BlogComment parent;
	private User user;

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public BlogComment getParent() {
		return parent;
	}

	public void setParent(BlogComment parent) {
		this.parent = parent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if(blog == null){
			throw new InvalidParamException();
		}
	}
}
