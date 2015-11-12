package me.qyh.oauth2.exception;

import me.qyh.oauth2.entity.OauthType;

public class Oauth2InvalidAccessTokenException extends Oauth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Oauth2InvalidAccessTokenException(OauthType type, String message) {
		super(type, message);
	}

}
