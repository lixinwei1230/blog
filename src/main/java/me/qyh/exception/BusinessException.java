package me.qyh.exception;

import me.qyh.bean.I18NMessage;

/**
 *
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private I18NMessage i18nMessage;

	public BusinessException(String code, Object... params) {
		this.i18nMessage = new I18NMessage(code, params);
	}

	public I18NMessage getI18nMessage() {
		return i18nMessage;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}
