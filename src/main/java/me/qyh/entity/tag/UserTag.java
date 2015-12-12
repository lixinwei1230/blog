package me.qyh.entity.tag;

import me.qyh.entity.Id;
import me.qyh.entity.User;

public class UserTag extends Id{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private Tag tag;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
}
