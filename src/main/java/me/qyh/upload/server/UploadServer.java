package me.qyh.upload.server;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;

public interface UploadServer {

	Object upload(List<MultipartFile> files) throws LogicException;
	
	File seekFile(String relativePath) throws MyFileNotFoundException;
	
	Object deleteFile(String ... paths) throws LogicException;
}
