package me.qyh.upload.server;

import me.qyh.entity.MyFile;

public interface FileStorage {
	
	String store(FileMapper mapper) throws Exception;
	
	String delUrl(MyFile file);
	
	String fileUrl(MyFile file);
	
	int id();
	
	boolean canStore(MyFile file);

}
