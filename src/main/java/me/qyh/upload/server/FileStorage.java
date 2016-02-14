package me.qyh.upload.server;

import me.qyh.bean.Info;
import me.qyh.entity.MyFile;

public interface FileStorage {
	
	String store(FileMapper mapper) throws Exception;
	
	Info del(MyFile file);
	
	String fileUrl(MyFile file);
	
	int id();
	
	boolean canStore(MyFile file);

}
