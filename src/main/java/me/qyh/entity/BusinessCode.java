package me.qyh.entity;

import java.util.Date;

public class BusinessCode {

	private String code;
	private User creater;
	private Date create;
	private Date expire;
	private Boolean alive;// 是否存活

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getCreater() {
		return creater;
	}

	public void setCreater(User creater) {
		this.creater = creater;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public boolean isExpire() {
		return expire == null ? false : new Date().after(expire);
	}

	public Boolean getAlive() {
		return alive;
	}

	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

}
