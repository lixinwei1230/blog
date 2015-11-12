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

	private List<FileStore> stores = new ArrayList<FileStore>();

	@Override
	public FileStore getStore(int id) {
		for (FileStore store : stores) {
			if (store.id() == id) {
				return store;
			}
		}
		return null;
	}

	public void setStores(List<FileStore> stores) {
		this.stores = stores;
	}

	@Override
	public FileStore getStore() {
		return stores.get(0);
	}

}
