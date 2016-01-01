package me.qyh.upload.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import me.qyh.entity.MyFile;
import me.qyh.exception.SystemException;

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
	public FileStorage getStore(MyFile file) {
		for (FileStorage store : stores) {
			if (store.canStore(file)) {
				return store;
			}
		}
		//
		throw new SystemException(String.format("无法找到能存放%s的存储器", file));
	}

}
