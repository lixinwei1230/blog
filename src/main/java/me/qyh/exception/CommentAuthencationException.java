package me.qyh.exception;

public class CommentAuthencationException extends LogicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentAuthencationException(String errorCode, Object... params) {
		super(errorCode, params);
	}

}
