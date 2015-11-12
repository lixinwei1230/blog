package me.qyh.security;

import org.springframework.security.core.GrantedAuthority;

import me.qyh.entity.RoleEnum;

public class RoleGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RoleEnum role;
	private Integer id;

	@Override
	public String getAuthority() {
		return role.name();
	}

	public Integer getId() {
		return id;
	}

	public RoleEnum getRole() {
		return role;
	}

	public RoleGrantedAuthority(RoleEnum role, Integer id) {
		this.role = role;
		this.id = id;
	}

}
