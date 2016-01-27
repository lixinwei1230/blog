package me.qyh.manage.service;

import me.qyh.exception.LogicException;
import me.qyh.server.TipMessage;
import me.qyh.service.MyFileService;

public interface MyFileManageService extends MyFileService {

	/**
	 * 删除文件，如果删除失败(文件被占用)则会等待job删除
	 * @param id
	 * @param tipMessage
	 * @throws LogicException
	 */
	void deleteMyFile(Integer id , TipMessage tipMessage) throws LogicException;
	
	/**
	 * 删除待删除文件
	 */
	void clear();

}
