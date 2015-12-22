package me.qyh.helper.cache;

import me.qyh.exception.SystemException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

public class NamedCache implements InitializingBean{

	@Autowired
	private CacheManager cacheManager;
	private String cacheName;
	private Cache cache ;
	
	public void put(Object key,Object value){
		cache.put(key, value);
	}
	
	public Object get(Object key){
		ValueWrapper vw = cache.get(key);
		if(vw != null){
			return vw.get();
		}
		return null;
	}
	
	public <T> T get(Object key , Class<T> clazz){
		return cache.get(key, clazz);
	}
	
	public void clear(){
		cache.clear();
	}
	
	public void evict(Object key){
		cache.evict(key);
	}
	
	public Object nativeCache(){
		return cache.getNativeCache();
	}
	
	public boolean hasKey(Object key){
		return get(key) != null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		cache = cacheManager.getCache(cacheName);
		if(cache == null){
			throw new SystemException(String.format("无法找到名为%s的缓存", cacheName));
		}
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
}
