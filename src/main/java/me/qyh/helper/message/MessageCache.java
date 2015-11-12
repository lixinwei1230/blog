package me.qyh.helper.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 用户读取未读信息数的缓存
 * 
 * @author mhlx
 *
 */
@Component("messageCache")
public class MessageCache {

	private static final String CACHE_NAME = "messageCache";
	private static final String KEY_PREFIX = "toReadMessageCount-";

	@Autowired
	private CacheManager cacheManager;

	private String generateKey(Integer userId) {
		return KEY_PREFIX + userId;
	}

	private Cache getCache() {
		return cacheManager.getCache(CACHE_NAME);
	}

	public void cache(Integer userId, int count) {
		getCache().put(generateKey(userId), count);
	}

	public boolean hasKey(Integer userId) {
		Cache cache = getCache();
		return cache.get(generateKey(userId)) != null;
	}

	public Integer get(Integer userId) {
		Cache cache = getCache();
		String key = generateKey(userId);
		ValueWrapper cached = cache.get(key);
		if (cached != null) {
			return (Integer) cached.get();
		}
		return 0;
	}

	public void addCount(Integer userId, int count) {
		Cache cache = getCache();
		String key = generateKey(userId);
		ValueWrapper cached = cache.get(key);
		if (cached != null) {
			int oldCount = (Integer) cached.get();
			cache.put(key, oldCount + count);
		}
	}

	public void cacheSource(String sourceKey, int count) {
		getCache().put(sourceKey, count);
	}

	public void clear() {
		getCache().clear();
	}
}
