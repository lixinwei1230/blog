package me.qyh.helper.file;

public class BadImageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadImageException(Throwable cause) {
		super(cause);
	}

	public BadImageException(String message) {
		super(message);
	}

}
