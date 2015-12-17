package me.qyh.page.widget;

import java.util.Date;

import me.qyh.entity.User;

public class UserWidget extends Widget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private Date createDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public UserWidget(){
		setType(WidgetType.USER);
	}
}
