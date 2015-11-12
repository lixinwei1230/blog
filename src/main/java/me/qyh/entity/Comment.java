package me.qyh.entity;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.helper.htmlclean.JsonHtmlXssSerializer;
import me.qyh.pageparam.Page;

/**
 * 评论
 * 
 * @author mhlx
 *
 */
public class Comment extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommentScope scope;// 评论域
	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String title;// 标题
	private String content;// 内容
	private User user;// 评论人
	private boolean isAnonymous;// 是否匿名评论
	private Date commentDate;// 评论日期
	private Comment parent;// 父评论
	private User replyTo;// 回复对象
	private Comment reply;// 回复评论
	private Page<Comment> page;// 子评论

	public CommentScope getScope() {
		return scope;
	}

	public void setScope(CommentScope scope) {
		this.scope = scope;
	}

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

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public User getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(User replyTo) {
		this.replyTo = replyTo;
	}

	public Page<Comment> getPage() {
		return page;
	}

	public void setPage(Page<Comment> page) {
		this.page = page;
	}

	public Comment getReply() {
		return reply;
	}

	public void setReply(Comment reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "Comment [scope=" + scope + ", title=" + title + ", content=" + content + ", user=" + user
				+ ", isAnonymous=" + isAnonymous + ", commentDate=" + commentDate + ", parent=" + parent + ", replyTo="
				+ replyTo + ", reply=" + reply + ", page=" + page + "]";
	}

}
