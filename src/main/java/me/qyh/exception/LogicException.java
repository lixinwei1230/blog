package me.qyh.exception;

import me.qyh.bean.I18NMessage;

public class LogicException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogicException(String errorCode, Object... param) {
		super(errorCode, param);
	}

	public LogicException(I18NMessage i18nMessage) {
		super(i18nMessage);
	}
	
}
