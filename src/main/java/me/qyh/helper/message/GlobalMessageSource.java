package me.qyh.helper.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.dao.MessageSendDao;
import me.qyh.entity.User;
import me.qyh.entity.message.MessageReceive;
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageStatus;
import me.qyh.entity.message.MessageType;

/**
 * 全局信息源
 * 
 * @author mhlx
 *
 */
public class GlobalMessageSource implements MessageSource {

	@Autowired
	private MessageSendDao messageSendDao;
	@Value("${config.message.globalMessageSource.provide.maxCount}")
	private int maxCount;

	@Override
	public List<MessageReceive> provideTosendMessages(User user) {
		List<MessageSend> messages = messageSendDao
				.selectUnSendMessageByTypeAndUser(MessageType.GLOBAL, user,
						maxCount);

		if (!messages.isEmpty()) {
			List<MessageReceive> receives = new ArrayList<MessageReceive>(
					messages.size());
			for (MessageSend send : messages) {
				MessageReceive receive = new MessageReceive();
				receive.setMessage(send);
				receive.setIsRead(false);
				receive.setReceiver(user);
				receive.setStatus(MessageStatus.COMMON);
				receives.add(receive);
			}
			return receives;
		}

		return Collections.emptyList();
	}

}
