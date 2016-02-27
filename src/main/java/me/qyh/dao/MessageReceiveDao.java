package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.message.MessageReceive;
import me.qyh.entity.message.MessageReceive.MessageStatus;
import me.qyh.pageparam.MessageReceivePageParam;

public interface MessageReceiveDao extends BaseDao<MessageReceive, Integer> {

	void inserts(List<MessageReceive> receives);

	List<MessageReceive> selectPage(MessageReceivePageParam param);

	int selectCount(MessageReceivePageParam param);

	void deleteByOverdaysAndStatus(@Param("days") int days, @Param("status") MessageStatus status);

}
