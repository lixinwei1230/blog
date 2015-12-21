package me.qyh.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

import me.qyh.oauth2.entity.OauthType;

public class Oauth2InvalidPrincipalException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OauthType type;

	public Oauth2InvalidPrincipalException(OauthType type, String message) {
		super(message);
		this.type = type;
	}

	public OauthType getType() {
		return type;
	}
	
}
