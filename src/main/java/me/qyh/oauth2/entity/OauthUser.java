package me.qyh.oauth2.entity;

import java.util.Date;

import me.qyh.entity.Id;
import me.qyh.entity.User;

public class OauthUser extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private String userid;
	private OauthType type;
	private Date createDate;
	private String nickname;

	public enum OauthType {
		QQ, // qq
		SINA
	}

	/**
	 * 用户头像，如果存在的话
	 */
	private OauthAvatar avatar;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public OauthType getType() {
		return type;
	}

	public void setType(OauthType type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OauthAvatar getAvatar() {
		return avatar;
	}

	public void setAvatar(OauthAvatar avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
