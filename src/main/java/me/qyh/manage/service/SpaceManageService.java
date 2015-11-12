package me.qyh.manage.service;

import me.qyh.exception.LogicException;
import me.qyh.server.TipMessage;
import me.qyh.service.SpaceService;

public interface SpaceManageService extends SpaceService {

	/**
	 * 禁用|解禁 用户空间
	 * 
	 * @param message
	 * @throws LogicException
	 */
	void toggleSpaceAbled(String id, TipMessage message) throws LogicException;

}
