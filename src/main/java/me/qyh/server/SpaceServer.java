package me.qyh.server;

import me.qyh.bean.Scopes;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.SpaceDisabledException;

public interface SpaceServer {

	Space getSpaceById(String id) throws DataNotFoundException, SpaceDisabledException;

	Space getSpaceByUser(User user) throws DataNotFoundException, SpaceDisabledException;

	/**
	 * 判断当前用户访问该空间的范围
	 * 
	 * @param visitor
	 *            当前用户
	 * @param toVisit
	 *            被访问的空间
	 * @return 不会为null
	 */
	Scopes getScopes(User visitor, Space toVisit) throws SpaceDisabledException;

}
