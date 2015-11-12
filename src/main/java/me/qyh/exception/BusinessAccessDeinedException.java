package me.qyh.exception;

public class BusinessAccessDeinedException extends BusinessRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CODE = "error.noAuthencation";

	public BusinessAccessDeinedException() {
		super(DEFAULT_CODE);
	}

	public BusinessAccessDeinedException(String code, Object... params) {
		super(code, params);
	}
}
