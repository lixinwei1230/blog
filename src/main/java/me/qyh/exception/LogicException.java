package me.qyh.exception;

public class LogicException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogicException(String errorCode, Object... param) {
		super(errorCode, param);
	}
}
