package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageType;
import me.qyh.pageparam.MessageSendPageParam;

public interface MessageSendDao extends BaseDao<MessageSend, Integer> {

	List<MessageSend> selectUnSendMessageByTypeAndUser(@Param("type") MessageType type, @Param("user") User user,
			@Param("maxCount") int maxCount);

	List<MessageSend> selectPage(MessageSendPageParam param);

	int selectCount(MessageSendPageParam param);
}
