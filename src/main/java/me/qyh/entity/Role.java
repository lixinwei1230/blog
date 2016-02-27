package me.qyh.entity;

public class Role extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RoleEnum role;

	public enum RoleEnum {
		ROLE_SUPERVISOR, ROLE_USER, ROLE_SPACE, ROLE_MESSAGER, ROLE_OAUTH
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}
}
