package me.qyh.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.MessageDetailDao;
import me.qyh.dao.MessageReceiveDao;
import me.qyh.dao.MessageSendDao;
import me.qyh.entity.message.MessageReceive.MessageStatus;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageSend.MessageType;
import me.qyh.helper.cache.NamedCache;
import me.qyh.manage.service.MessageManageService;

@Service
public class MessageManageServiceImpl implements MessageManageService {

	@Autowired
	private MessageDetailDao messageDetailDao;
	@Autowired
	private MessageSendDao messageSendDao;
	@Autowired
	private MessageReceiveDao messageReceiveDao;
	@Autowired
	private NamedCache globalMessageCache;
	@Value("${config.message.overday}")
	private int overday;

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

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteMesssageReceiveByOverdays() {
		messageReceiveDao.deleteByOverdaysAndStatus(overday, MessageStatus.COMMON);
	}

}
