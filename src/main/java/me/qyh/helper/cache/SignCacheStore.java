package me.qyh.helper.cache;

import me.qyh.exception.SystemException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class SignCacheStore implements  InitializingBean{
	
	private static final String SIGN_CACHE = "signCache";
	private String name = SIGN_CACHE;
	
	@Autowired
	private CacheManager cacheManager;
	private Cache cache;
	
	public Sign getSign(Object key){
		return cache.get(key, Sign.class);
	}
	
	public void put(Object key,Sign sign){
		cache.put(key, sign);
	}
	
	public void evict(Object key){
		cache.evict(key);
	}
	
	public void clear(){
		cache.clear();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.cache = cacheManager.getCache(name);
		if(cache == null){
			throw new SystemException("无法找到名为"+name+"的缓存");
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
