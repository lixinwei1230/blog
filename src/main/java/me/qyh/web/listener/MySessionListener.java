package me.qyh.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import me.qyh.entity.User;
import me.qyh.helper.cache.NamedCache;
import me.qyh.security.UserContext;
import me.qyh.web.SpringContextHolder;

public class MySessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent e) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		User user = UserContext.getUser();
		if(user != null){
			SpringContextHolder.getBean("messageCache", NamedCache.class).evict(user.getId());
		}
	}
}
