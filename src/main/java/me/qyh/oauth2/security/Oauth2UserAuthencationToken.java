package me.qyh.oauth2.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class Oauth2UserAuthencationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Object principal;

	public Oauth2UserAuthencationToken(Object principal,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		setAuthenticated(true);
	}

	public Oauth2UserAuthencationToken(Object principal) {
		super(null);
		this.principal = principal;
		setAuthenticated(false);
	}

	/**
	 * 这里不需要获取用户的密码
	 */
	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}
