package me.qyh.helper.message;

import java.util.List;

import me.qyh.entity.User;
import me.qyh.entity.message.MessageReceive;

/**
 * 信息源
 * 
 * @author mhlx
 *
 */
public interface MessageSource {

	/**
	 * 根据用户返回需要向该用户发送的信息
	 * 
	 * @param user
	 * @return
	 */
	List<MessageReceive> provideTosendMessages(User user);

}
