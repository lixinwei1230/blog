package me.qyh.exception;

public class MyFileNotFoundException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyFileNotFoundException() {
		this("error.file.notexists");
	}

	public MyFileNotFoundException(String code, Object... params) {
		super(code, params);
	}

}
