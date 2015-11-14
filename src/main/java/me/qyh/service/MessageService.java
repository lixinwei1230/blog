package me.qyh.service;

import java.util.Set;

import me.qyh.entity.User;
import me.qyh.entity.message.MessageReceive;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageSendDetail;
import me.qyh.entity.message.MessageStatus;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MessageReceivePageParam;
import me.qyh.pageparam.MessageSendPageParam;
import me.qyh.pageparam.Page;

public interface MessageService {

	int getToReadMessageCount(User user, Set<Integer> sourceIds);

	void insertMessage(MessageSendDetail message) throws LogicException;

	Page<MessageSend> findMessageSends(MessageSendPageParam param);

	MessageSend getMessageSend(Integer id) throws LogicException;

	Page<MessageReceive> findMessageReceives(MessageReceivePageParam param);

	MessageReceive getMessageReceive(Integer id) throws LogicException;

	void updateIsRead(Set<Integer> receives, boolean isRead) throws LogicException;

	void deleteMessageReceives(Set<Integer> receives) throws LogicException;

	void updateMessageRecieveStatus(Set<Integer> receives, MessageStatus status) throws LogicException;

	void deleteMessageSends(Set<Integer> sends) throws LogicException;

}
