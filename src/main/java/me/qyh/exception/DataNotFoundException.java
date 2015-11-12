package me.qyh.exception;

public class DataNotFoundException extends LogicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String errorCode, Object... param) {
		super(errorCode, param);
	}

}
