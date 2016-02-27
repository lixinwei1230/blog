package me.qyh.entity;

import java.util.Date;

/**
 * 用户注册，忘记密码时候发送的码值
 * 
 * @author mhlx
 *
 */
public class UserCode extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;// 码
	private User user;// 用户
	private UserCodeType type;// 码类型
	private Date createDate;// 发送日期
	private Boolean alive;// 是否存活

	public enum UserCodeType {

		ACTIVATE, // 激活
		FORGETPASSWORD, // 忘记密码
		OAUTH_BIND, // 绑定
		OAUTH_AUTHORIZE_EMAIIL, // 确认用户提供的email属于该用户
		OAUTH_COMPLETE_USERINFO// 完善用户信息
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserCodeType getType() {
		return type;
	}

	public void setType(UserCodeType type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getAlive() {
		return alive;
	}

	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

	public UserCode(String code, User user, UserCodeType type) {
		this.code = code;
		this.user = user;
		this.type = type;
		this.alive = true;
		this.createDate = new Date();
	}

	public UserCode() {
	}

}
