package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.MessageDetailDao;
import me.qyh.dao.MessageReceiveDao;
import me.qyh.dao.MessageSendDao;
import me.qyh.entity.User;
import me.qyh.entity.message.MessageDetail;
import me.qyh.entity.message.MessageReceive;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageSendDetail;
import me.qyh.entity.message.MessageStatus;
import me.qyh.entity.message.MessageType;
import me.qyh.exception.LogicException;
import me.qyh.helper.cache.NamedCache;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.pageparam.MessageReceivePageParam;
import me.qyh.pageparam.MessageSendPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.server.UserServer;
import me.qyh.service.MessageService;
import me.qyh.utils.Validators;

@Service
@SuppressWarnings("unchecked")
public class MessageServiceImpl extends BaseServiceImpl implements MessageService, TipServer, InitializingBean {

	@Autowired
	protected MessageSendDao messageSendDao;
	@Autowired
	private MessageReceiveDao messageReceiveDao;
	@Autowired
	protected MessageDetailDao messageDetailDao;
	@Autowired
	private HtmlContentHandler messageHtmlHandler;
	@Autowired
	private UserServer userServer;
	@Value("${config.message.globalMessageSource.provide.maxCount}")
	private int maxCount;
	@Autowired
	private NamedCache messageCache;
	@Autowired
	private NamedCache globalMessageCache;

	private static final String GLOBAL_MESSAGE = "globalMessage";
	private static final String UNREAD_COUNT = "unreadCount";

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int getToReadMessageCount(User user, Set<Integer> sourceIds) {
		int unreadFromSources = handleGlobalMessage(user);
		Map<String, Object> map = (Map<String, Object>) messageCache.get(user.getId());
		if (map != null && map.containsKey(UNREAD_COUNT)) {
			int totalCount = (int) map.get(UNREAD_COUNT) + unreadFromSources;
			addCount(user, unreadFromSources);
			return totalCount;
		}

		MessageReceivePageParam param = new MessageReceivePageParam();
		param.setIsRead(false);
		param.setReceiver(user);
		int count = messageReceiveDao.selectCount(param);
		addCount(user, count);
		return count;
	}

	private int handleGlobalMessage(User user) {
		List<MessageSend> messages = null;
		List<MessageSend> all = (List<MessageSend>) globalMessageCache.get(GLOBAL_MESSAGE);
		Map<String, Object> map = (Map<String, Object>) messageCache.get(user.getId());
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		if (!map.containsKey(GLOBAL_MESSAGE)) {
			messages = messageSendDao.selectUnSendMessageByTypeAndUser(MessageType.GLOBAL, user, maxCount);
		} else {
			List<MessageSend> received = (List<MessageSend>) map.get(GLOBAL_MESSAGE);
			if (!Validators.isEmptyOrNull(received)) {
				messages = getUnrecieve(received, all);
			} else {
				messages = all;
			}
		}
		if (!messages.isEmpty()) {
			List<MessageReceive> receives = new ArrayList<MessageReceive>(messages.size());
			for (MessageSend send : messages) {
				MessageReceive receive = new MessageReceive();
				receive.setMessage(send);
				receive.setIsRead(false);
				receive.setReceiver(user);
				receive.setStatus(MessageStatus.COMMON);
				receives.add(receive);
			}
			messageReceiveDao.inserts(receives);
		}
		map.put(GLOBAL_MESSAGE, new ArrayList<MessageSend>(all));
		messageCache.put(user.getId(), map);
		return messages.size();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertMessage(MessageSendDetail message) throws LogicException {
		messageDetailDao.insert(message.getDetail());
		messageSendDao.insert(message);
		for (String username : message.getReceivers()) {
			User user = userServer.getUserByNameOrEmail(username);
			MessageReceive receive = new MessageReceive();
			receive.setMessage(message);
			receive.setIsRead(false);
			receive.setReceiver(user);
			receive.setMessage(message);
			receive.setStatus(MessageStatus.COMMON);
			messageReceiveDao.insert(receive);
			addCount(user, 1);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MessageSend> findMessageSends(MessageSendPageParam param) {
		int total = messageSendDao.selectCount(param);
		List<MessageSend> datas = messageSendDao.selectPage(param);
		return new Page<MessageSend>(param, total, datas);
	}

	@Override
	@Transactional(readOnly = true)
	public MessageSend getMessageSend(Integer id) throws LogicException {
		MessageSend send = loadMessageSend(id);
		super.doAuthencation(UserContext.getUser(), send.getSender());
		cleanMessageDetail(send.getDetail());
		return send;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateIsRead(Set<Integer> receives, boolean isRead) throws LogicException {
		for (Integer id : receives) {
			MessageReceive receive = loadMessageReceive(id);
			super.doAuthencation(UserContext.getUser(), receive.getReceiver());

			boolean _isRead = receive.getIsRead();

			if (!Boolean.valueOf(receive.getIsRead()).equals(isRead)) {
				receive.setIsRead(isRead);
				messageReceiveDao.update(receive);
			}

			if (!_isRead && isRead) {
				addCount(receive.getReceiver(), -1);
			}

			if (_isRead && !isRead) {
				addCount(receive.getReceiver(), 1);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteMessageReceives(Set<Integer> receives) throws LogicException {
		for (Integer id : receives) {
			MessageReceive receive = loadMessageReceive(id);
			super.doAuthencation(UserContext.getUser(), receive.getReceiver());

			messageReceiveDao.deleteById(id);

			if (!receive.getIsRead()) {
				addCount(receive.getReceiver(), -1);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteMessageSends(Set<Integer> sends) throws LogicException {
		for (Integer id : sends) {
			MessageSend send = loadMessageSend(id);
			super.doAuthencation(UserContext.getUser(), send.getSender());

			messageSendDao.deleteById(id);
			
			if(MessageType.GLOBAL.equals(send.getType())){
				deleteMessageSend(send);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateMessageRecieveStatus(Set<Integer> receives, MessageStatus status) throws LogicException {
		for (Integer id : receives) {
			MessageReceive receive = loadMessageReceive(id);
			super.doAuthencation(UserContext.getUser(), receive.getReceiver());

			if (!receive.getStatus().equals(status)) {
				receive.setStatus(status);
				messageReceiveDao.update(receive);
			}

		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MessageReceive> findMessageReceives(MessageReceivePageParam param) {

		if (param.getSend() != null) {
			MessageSend send = messageSendDao.selectById(param.getSend().getId());
			if (send == null) {
				return new Page<MessageReceive>(param, 0, new ArrayList<MessageReceive>());
			}

			super.doAuthencation(UserContext.getUser(), send.getSender());
		}

		int total = messageReceiveDao.selectCount(param);
		List<MessageReceive> datas = messageReceiveDao.selectPage(param);
		return new Page<MessageReceive>(param, total, datas);
	}

	@Override
	@Transactional(readOnly = true)
	public MessageReceive getMessageReceive(Integer id) throws LogicException {
		MessageReceive receive = loadMessageReceive(id);
		super.doAuthencation(UserContext.getUser(), receive.getReceiver());

		cleanMessageDetail(receive.getMessage().getDetail());

		return receive;
	}

	private void cleanMessageDetail(MessageDetail detail) {
		detail.setContent(messageHtmlHandler.handle(detail.getContent()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public void sendTip(TipMessage message) {
		if (message.getSender().equals(message.getReceiver())) {
			return;
		}

		MessageDetail detail = new MessageDetail(message.getTitle(), message.getContent());
		messageDetailDao.insert(detail);

		MessageSend toSend = new MessageSend();
		toSend.setDetail(detail);
		toSend.setSendDate(new Date());

		toSend.setSender(message.getSender());
		toSend.setType(MessageType.SYSTEM);
		messageSendDao.insert(toSend);

		MessageReceive toReceive = new MessageReceive();
		toReceive.setMessage(toSend);
		toReceive.setReceiver(message.getReceiver());
		toReceive.setStatus(MessageStatus.COMMON);
		messageReceiveDao.insert(toReceive);

		addCount(message.getReceiver(), 1);

	}

	private MessageReceive loadMessageReceive(Integer id) throws LogicException {
		MessageReceive receive = messageReceiveDao.selectById(id);
		if (receive == null) {
			throw new LogicException("error.message.notFound");
		}

		return receive;
	}

	private MessageSend loadMessageSend(Integer id) throws LogicException {
		MessageSend send = messageSendDao.selectById(id);
		if (send == null) {
			throw new LogicException("error.message.notFound");
		}

		return send;
	}

	private void addCount(User user, int count) {
		int _count = 0;
		Map<String, Object> map = (Map<String, Object>) messageCache.get(user.getId());
		if(map != null){
			if(map.containsKey(UNREAD_COUNT)){
				_count = (int) map.get(UNREAD_COUNT);
			}
		} else {
			map = new HashMap<String , Object>();
		}
		_count += count;
		map.put(UNREAD_COUNT, _count);
		messageCache.put(user.getId(), map);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		MessageSendPageParam param = new MessageSendPageParam();
		param.setCurrentPage(1);
		param.setPageSize(maxCount);
		param.setType(MessageType.GLOBAL);
		List<MessageSend> globalMessages = messageSendDao.selectPage(param);
		globalMessageCache.put(GLOBAL_MESSAGE, globalMessages);
	}

	private List<MessageSend> getUnrecieve(List<MessageSend> received, List<MessageSend> all) {
		List<MessageSend> unreceive = new ArrayList<MessageSend>();
		for (MessageSend send : all) {
			if (!received.contains(send)) {
				unreceive.add(send);
			}
		}
		return unreceive;
	}

	private void deleteMessageSend(MessageSend toDelete) {
		List<MessageSend> all = (List<MessageSend>) globalMessageCache.get(GLOBAL_MESSAGE);
		if(all.contains(toDelete)){
			int pos = all.indexOf(toDelete);
			all.remove(pos);
		}
	}

}
