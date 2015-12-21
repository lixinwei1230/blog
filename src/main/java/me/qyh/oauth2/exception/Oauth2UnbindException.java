package me.qyh.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

import me.qyh.oauth2.security.OauthPrincipal;

public class Oauth2UnbindException extends AuthenticationException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OauthPrincipal principal;

	public Oauth2UnbindException(OauthPrincipal principal) {
		super("");
		this.principal = principal;
	}

	public OauthPrincipal getPrincipal() {
		return principal;
	}
	
}
