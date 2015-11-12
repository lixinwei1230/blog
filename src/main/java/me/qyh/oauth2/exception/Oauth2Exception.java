package me.qyh.oauth2.exception;

import me.qyh.exception.SystemException;
import me.qyh.oauth2.entity.OauthType;

public class Oauth2Exception extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OauthType type;

	public Oauth2Exception(OauthType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public Oauth2Exception(OauthType type, String message) {
		super(message);
		this.type = type;
	}

	public Oauth2Exception(OauthType type, Throwable cause) {
		super(cause);
		this.type = type;
	}

	public OauthType getType() {
		return type;
	}

}
