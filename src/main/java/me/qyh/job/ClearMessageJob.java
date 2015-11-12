package me.qyh.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.dao.MessageReceiveDao;
import me.qyh.entity.message.MessageStatus;
import me.qyh.helper.message.MessageCache;

public class ClearMessageJob {

	private final Logger logger = LoggerFactory.getLogger("errorLogger");

	@Autowired
	private MessageReceiveDao messageReceiveDao;
	@Autowired
	private MessageCache messageCache;
	@Value("${config.message.overday}")
	private int overday;

	public synchronized void doJob() {
		try {
			messageCache.clear();
			messageReceiveDao.deleteByOverdaysAndStatus(overday, MessageStatus.COMMON);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
