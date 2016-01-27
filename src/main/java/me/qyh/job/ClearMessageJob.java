package me.qyh.job;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.helper.cache.NamedCache;
import me.qyh.manage.service.MessageManageService;

public class ClearMessageJob {

	@Autowired
	private MessageManageService messageManageService;
	@Autowired
	private NamedCache messageCache;

	public synchronized void doJob() {
		messageCache.clear();
		messageManageService.deleteMesssageReceiveByOverdays();
	}

}
