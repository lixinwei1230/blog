package me.qyh.manage.service;

import me.qyh.exception.LogicException;
import me.qyh.server.TipMessage;
import me.qyh.service.MyFileService;

public interface MyFileManageService extends MyFileService {

	void deleteMyFile(Integer id, TipMessage tipMessage) throws LogicException;

}
