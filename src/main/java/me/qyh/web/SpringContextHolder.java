package me.qyh.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author henry.qian
 *
 */
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext application) throws BeansException {
		SpringContextHolder.applicationContext = application;
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}
	
	public static <T> Map<String,T> getBeansOfType(Class<T> clazz){
		Map<String,T> map = new HashMap<String,T>();
		map.putAll(applicationContext.getBeansOfType(clazz));
		if(applicationContext.getParent() != null){
			map.putAll(applicationContext.getParent().getBeansOfType(clazz));
		}
		return map;
	}
}
