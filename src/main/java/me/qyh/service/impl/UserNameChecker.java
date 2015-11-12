package me.qyh.service.impl;

import me.qyh.exception.LogicException;

public interface UserNameChecker {

	/**
	 * 检查用户名是否被允许
	 * 
	 * @param username
	 *            用户名
	 * @throws LogicException
	 *             如果用户名不被允许，则抛出该异常
	 */
	public void check(String username) throws LogicException;

}
