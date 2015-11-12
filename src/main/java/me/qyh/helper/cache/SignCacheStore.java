package me.qyh.helper.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Component;

@Component
public class SignCacheStore {

	private Map<Object, Sign> map = new ConcurrentHashMap<Object, Sign>(256);

	public void addSign(Object key, Sign sign) {
		map.put(key, sign);
	}

	public Sign getSign(Object key) {
		return map.get(key);
	}

	public void remove(Object key) {
		map.remove(key);
	}

	public synchronized void clear() {
		long now = new Date().getTime();
		List<Object> deletes = new ArrayList<Object>();
		for (Map.Entry<Object, Sign> m : map.entrySet()) {
			Sign sign = m.getValue();
			if (sign != null) {
				Sign cloned = (Sign) SerializationUtils.clone(sign);
				if (!cloned.addHit(now)) {
					deletes.add(m.getKey());
				}
			}
		}
		for (Object delete : deletes) {
			map.remove(delete);
		}
	}

}
