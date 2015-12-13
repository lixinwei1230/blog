package me.qyh.entity;

import java.util.Date;

public class LoginInfo extends Id{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String remoteAddress;
	private Date loginDate;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
}
