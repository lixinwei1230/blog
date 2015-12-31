package me.qyh.upload.server.inner;

import me.qyh.entity.MyFile;
import me.qyh.upload.server.FileStorage;

public interface FileMapping {

	String mapping(MyFile file,FileStorage store);
	
}
