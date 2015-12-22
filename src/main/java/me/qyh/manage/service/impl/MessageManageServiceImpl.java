package me.qyh.manage.service.impl;

import me.qyh.dao.MessageDetailDao;
import me.qyh.dao.MessageSendDao;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageType;
import me.qyh.helper.cache.NamedCache;
import me.qyh.manage.service.MessageManageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageManageServiceImpl  implements MessageManageService{
	
	@Autowired
	private MessageDetailDao messageDetailDao;
	@Autowired
	private MessageSendDao messageSendDao;
	@Autowired
	private NamedCache globalMessageCache;
	
	private static final String GLOBAL_MESSAGE = "globalMessage";

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertGlobalMessage(MessageSend message) {
		messageDetailDao.insert(message.getDetail());
		message.setType(MessageType.GLOBAL);
		messageSendDao.insert(message);
		
		@SuppressWarnings("unchecked")
		List<MessageSend> all = (List<MessageSend>) globalMessageCache.get(GLOBAL_MESSAGE);
		all.add(message);
	}

}
