package me.qyh.helper.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("signCacheStoreClear")
public class SignCacheStoreClear {

	@Autowired
	private SignCacheStore signCacheStore;

	public void doJob() {
		signCacheStore.clear();
	}

}
