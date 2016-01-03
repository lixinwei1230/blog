package me.qyh.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.helper.htmlclean.JsonHtmlXssSerializer;

/**
 * 评论域
 * 
 * @author mhlx
 *
 */
public class CommentScope extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String scopeId;// 域ID
	private String scope;// 域名
	private User user;

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CommentScope [scopeId=" + scopeId + ", scope=" + scope + "]";
	}

}
