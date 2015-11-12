package me.qyh.exception;

/**
 * 系统异常，主要用来处理系统的bug，无需向用户反馈具体信息
 * 
 * @author henry.qian
 *
 */
public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

}
