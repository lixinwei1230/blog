package me.qyh.upload.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * 
 * @author qyh
 *
 */
@Component(value = "fileServer")
public class MemoryServer implements FileServer {

	private List<FileStorage> stores = new ArrayList<FileStorage>();

	@Override
	public FileStorage getStore(int id) {
		for (FileStorage store : stores) {
			if (store.id() == id) {
				return store;
			}
		}
		return null;
	}

	public void setStores(List<FileStorage> stores) {
		this.stores = stores;
	}

	@Override
	public FileStorage getStore() {
		return stores.get(0);
	}

}
