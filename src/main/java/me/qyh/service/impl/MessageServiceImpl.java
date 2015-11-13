package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.helper.message.MessageCache;
import me.qyh.helper.message.MessageSource;
import me.qyh.pageparam.MessageReceivePageParam;
import me.qyh.pageparam.MessageSendPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.server.UserServer;
import me.qyh.service.MessageService;

public class MessageServiceImpl extends BaseServiceImpl implements MessageService, TipServer {

	@Autowired
	private MessageSendDao messageSendDao;
	@Autowired
	private MessageReceiveDao messageReceiveDao;
	@Autowired
	private MessageDetailDao messageDetailDao;
	@Autowired
	private HtmlContentHandler messageHtmlHandler;
	@Autowired
	private MessageCache messageCache;
	@Autowired
	private UserServer userServer;
	private Map<Integer, MessageSource> sources = new HashMap<Integer, MessageSource>();

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int getToReadMessageCount(User user, Set<Integer> sourceIds) {
		int unreadFromSources = handleMessageSources(user, sourceIds);

		Integer userId = user.getId();
		if (messageCache.hasKey(userId)) {
			int totalCount = messageCache.get(userId) + unreadFromSources;
			messageCache.addCount(userId, unreadFromSources);

			return totalCount;
		}

		MessageReceivePageParam param = new MessageReceivePageParam();
		param.setIsRead(false);
		param.setReceiver(user);
		int count = messageReceiveDao.selectCount(param);
		messageCache.cache(userId, count);
		return count;
	}

	private int handleMessageSources(User user, Set<Integer> sourceIds) {
		List<MessageSource> _sources = new ArrayList<MessageSource>();
		for (int sourceId : sourceIds) {
			if (sources.containsKey(sourceId)) {
				_sources.add(sources.get(sourceId));
			} else {
				throw new SystemException("无法找到信息源:" + sourceId);
			}
		}
		List<MessageReceive> toReceive = new ArrayList<MessageReceive>();
		if (!_sources.isEmpty()) {
			for (MessageSource source : _sources) {
				toReceive.addAll(source.provideTosendMessages(user));
			}
		}
		if (!toReceive.isEmpty()) {
			messageReceiveDao.inserts(toReceive);
		}
		return toReceive.size();
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
			messageCache.addCount(user.getId(), 1);
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
	public MessageSend getMessageSend(Integer id) throws DataNotFoundException {
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
				messageCache.addCount(receive.getReceiver().getId(), -1);
			}

			if (_isRead && !isRead) {
				messageCache.addCount(receive.getReceiver().getId(), 1);
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
				messageCache.addCount(receive.getReceiver().getId(), -1);
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
	public MessageReceive getMessageReceive(Integer id) throws DataNotFoundException {
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

		messageCache.addCount(message.getReceiver().getId(), 1);

	}

	private MessageReceive loadMessageReceive(Integer id) throws DataNotFoundException {
		MessageReceive receive = messageReceiveDao.selectById(id);
		if (receive == null) {
			throw new DataNotFoundException("error.message.notFound");
		}

		return receive;
	}

	private MessageSend loadMessageSend(Integer id) throws DataNotFoundException {
		MessageSend send = messageSendDao.selectById(id);
		if (send == null) {
			throw new DataNotFoundException("error.message.notFound");
		}

		return send;
	}

	public void setSources(Map<Integer, MessageSource> sources) {
		this.sources = sources;
	}

}
