package me.qyh.pageparam;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import me.qyh.entity.RoleEnum;

public class UserPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;// 用户名
	private String nickname;// 昵称
	private Date begin;// 注册开始时间
	private Date end;// 注册结束时间
	private Set<RoleEnum> ignoreRoles = new HashSet<RoleEnum>();
	private Boolean enabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Set<RoleEnum> getIgnoreRoles() {
		return ignoreRoles;
	}

	public void addIgnoreRole(RoleEnum... roles) {
		Collections.addAll(this.ignoreRoles, roles);
	}

	public void setIgnoreRoles(Set<RoleEnum> ignoreRoles) {
		this.ignoreRoles = ignoreRoles;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
