package me.qyh.web.controller;

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

@RequestMapping("clean")
@Controller
public class CleanAvatarController {

	@Autowired
	private FileStore avatarStore;
	@Autowired
	private UploadServer avatarUploadServer;

	@ResponseBody
	@RequestMapping("avatar/deletePhysical")
	public Info deleteAvatar(@RequestParam("path") String path, @RequestParam("key") String key) {
		if (!avatarStore.delKey().equals(key)) {
			return new Info(false);
		}
		try {
			File file = avatarUploadServer.seekFile(path);
			return new Info(FileUtils.deleteQuietly(file));
		} catch (MyFileNotFoundException e) {

		}
		return new Info(true);
	}

}
