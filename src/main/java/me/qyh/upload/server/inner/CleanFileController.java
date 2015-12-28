package me.qyh.upload.server.inner;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.upload.server.FileStore;
import me.qyh.upload.server.UploadServer;

@Controller
@RequestMapping("clean")
public class CleanFileController{
	
	@Autowired
	private FileStore innerFileStore;
	@Autowired
	private UploadServer innerFileUploadServer;
	
	@ResponseBody
	@RequestMapping("file/deletePhysical")
	public Info deleteFile(@RequestParam("path") String path,@RequestParam("key") String key){
		if(!innerFileStore.delKey().equals(key)){
			return new Info(false);
		}
		try {
			File file = innerFileUploadServer.seekFile(path);
			return new Info(FileUtils.deleteQuietly(file));
		} catch (MyFileNotFoundException e) {
			
		}
		return new Info(true);
	}
}
