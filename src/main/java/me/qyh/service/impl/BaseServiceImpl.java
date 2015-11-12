package me.qyh.service.impl;

import me.qyh.bean.Scopes;
import me.qyh.entity.Scope;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.BusinessAccessDeinedException;

class BaseServiceImpl {

	protected void doAuthencation(Space current, Space toCheck) {
		if (current == null || !current.equals(toCheck)) {
			throw new BusinessAccessDeinedException();
		}
	}

	/**
	 * 验证操作权限
	 * 
	 * @param current
	 *            当前操作用户
	 * @param toCheck
	 *            记录拥有者
	 */
	protected void doAuthencation(User current, User toCheck) {
		if (current == null || !current.equals(toCheck)) {
			throw new BusinessAccessDeinedException();
		}
	}

	/**
	 * 验证访问权限
	 * 
	 * @param scopes
	 * @param toCheck
	 */
	protected void doAuthencation(Scopes scopes, Scope toCheck) {
		if (!scopes.hasScope(toCheck)) {
			throw new BusinessAccessDeinedException();
		}
	}
}
