package me.qyh.job;

import me.qyh.dao.MessageReceiveDao;
import me.qyh.entity.message.MessageStatus;
import me.qyh.helper.cache.NamedCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ClearMessageJob {

	private final Logger logger = LoggerFactory.getLogger(ClearMessageJob.class);

	@Autowired
	private MessageReceiveDao messageReceiveDao;
	@Autowired
	private NamedCache messageCache;
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
