package me.qyh.oauth2.exception;

import me.qyh.oauth2.entity.OauthType;

public class Oauth2ConnectionException extends Oauth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Oauth2ConnectionException(OauthType type, String message, Throwable cause) {
		super(type, message, cause);
	}

	public Oauth2ConnectionException(OauthType type, String message) {
		super(type, message);
	}

	public Oauth2ConnectionException(OauthType type, Throwable cause) {
		super(type, cause);
	}

}
