package me.qyh.job;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.manage.service.BlogManageService;

public class ScheduledBlogJob {

	@Autowired
	private BlogManageService blogManageService;

	public synchronized void doJob() {
		blogManageService.pubScheduled();
	}

}
