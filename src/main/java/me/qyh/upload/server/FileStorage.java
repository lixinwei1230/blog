package me.qyh.upload.server;

import java.io.File;

import me.qyh.bean.Info;
import me.qyh.entity.MyFile;

public interface FileStorage {

	String store(MyFile mf, File file) throws Exception;

	Info del(MyFile file);

	String fileUrl(MyFile file);

	int id();

	boolean canStore(MyFile file);

}
