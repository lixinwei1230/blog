package me.qyh.oauth2.exception;

import me.qyh.oauth2.entity.OauthType;

public class Oauth2InvalidStateException extends Oauth2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Oauth2InvalidStateException(OauthType type, String message) {
		super(type, message);
	}

}
