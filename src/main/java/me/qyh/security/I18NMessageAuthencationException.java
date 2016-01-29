package me.qyh.security;

import me.qyh.bean.I18NMessage;

import org.springframework.security.core.AuthenticationException;

public class I18NMessageAuthencationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private I18NMessage message;

	public I18NMessageAuthencationException(String errorCode, Object... params) {
		super(errorCode);
		this.message = new I18NMessage(errorCode, params);
	}

	public I18NMessage getI18NMessage() {
		return message;
	}

}
