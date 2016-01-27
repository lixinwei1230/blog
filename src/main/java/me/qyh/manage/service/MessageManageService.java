package me.qyh.manage.service;

import me.qyh.entity.message.MessageSend;

public interface MessageManageService {
	
	void insertGlobalMessage(MessageSend message);
	
	void deleteMesssageReceiveByOverdays();

}
