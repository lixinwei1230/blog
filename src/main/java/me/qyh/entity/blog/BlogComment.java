package me.qyh.entity.blog;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.entity.Id;
import me.qyh.entity.User;
import me.qyh.helper.html.JsonHtmlXssSerializer;
import me.qyh.pageparam.Page;

/**
 * 评论
 * 
 * @author mhlx
 *
 */
public class BlogComment extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Blog blog;
	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String title;// 标题
	private String content;// 内容
	private User user;// 评论人
	private boolean isAnonymous;// 是否匿名评论
	private Date commentDate;// 评论日期
	private BlogComment parent;// 父评论
	private User replyTo;// 回复对象
	private BlogComment reply;// 回复评论
	private Page<BlogComment> page;// 子评论

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public BlogComment getParent() {
		return parent;
	}

	public void setParent(BlogComment parent) {
		this.parent = parent;
	}

	public User getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(User replyTo) {
		this.replyTo = replyTo;
	}

	public Page<BlogComment> getPage() {
		return page;
	}

	public void setPage(Page<BlogComment> page) {
		this.page = page;
	}

	public BlogComment getReply() {
		return reply;
	}

	public void setReply(BlogComment reply) {
		this.reply = reply;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

}
