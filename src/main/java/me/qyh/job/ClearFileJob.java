package me.qyh.job;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.manage.service.MyFileManageService;

/**
 * 清理文件的定时任务
 * 
 * @author mhlx
 * 
 */
public class ClearFileJob {

	@Autowired
	private MyFileManageService myFileManageService;
	@Value("${config.tempdir}")
	private String tempdir;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;


	public synchronized void doJob() {
		FileUtils.deleteQuietly(new File(tempdir));
		FileUtils.deleteQuietly(new File(imageCacheDir));
		myFileManageService.clear();
	}
}
