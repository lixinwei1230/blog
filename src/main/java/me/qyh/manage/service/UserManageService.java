package me.qyh.manage.service;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserPageParam;
import me.qyh.server.TipMessage;
import me.qyh.service.UserService;

public interface UserManageService extends UserService {

	Page<User> findUsers(UserPageParam param);

	/**
	 * 将用户置为可用|不可用
	 * 
	 * @param message
	 * @throws LogicException
	 */
	void toggleUserAbled(int id, TipMessage message) throws LogicException;

}
