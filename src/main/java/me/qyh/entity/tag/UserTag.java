package me.qyh.entity.tag;

import me.qyh.entity.User;

public class UserTag extends WebTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private WebTag tag;

	public WebTag getTag() {
		return tag;
	}

	public void setTag(WebTag tag) {
		this.tag = tag;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
