package me.qyh.exception;

import me.qyh.entity.Space;

/**
 * 用户不可用
 * 
 * @author mhlx
 *
 */
public class SpaceDisabledException extends BusinessRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Space space;

	public Space getSpace() {
		return space;
	}

	public SpaceDisabledException(Space space, String errorCode, Object... params) {
		super(errorCode, params);
		this.space = space;
	}
}
