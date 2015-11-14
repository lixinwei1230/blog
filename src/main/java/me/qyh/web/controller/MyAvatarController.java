package me.qyh.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.bean.Crop;
import me.qyh.bean.Info;
import me.qyh.entity.MyFile;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.service.MyFileService;
import me.qyh.service.UserService;
import me.qyh.upload.server.UploadServer;
import me.qyh.upload.server.inner.InnerFileStore;
import me.qyh.utils.Files;

@Controller
public class MyAvatarController extends BaseController {

	private static final String AVATAR = "avatar";
	private static final String UPLOAD_URL = "uploadUrl";
	private static final String STORE = "store";

	@Autowired
	private UploadServer avatarUploadServer;
	@Autowired
	private InnerFileStore avatarStore;
	@Autowired
	private UserService userService;
	@Autowired
	private MyFileService myFileService;

	@RequestMapping(value = "my/avatar/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute(STORE, avatarStore.id());
		model.addAttribute(UPLOAD_URL, avatarStore.uploadUrl());
		return "my/avatar/index";
	}

	@RequestMapping(value = "my/avatar/upload", method = RequestMethod.POST)
	@ResponseBody
	public Info upload(@RequestParam(value = "file") MultipartFile file, HttpSession session) throws LogicException {
		session.setAttribute(AVATAR,
				avatarUploadServer.upload(new ArrayList<MultipartFile>(Arrays.asList(new MultipartFile[] { file }))));
		return new Info(true);
	}

	@RequestMapping(value = "my/avatar/choose", method = RequestMethod.GET)
	@ResponseBody
	public Info choose(@RequestParam(value = "id") int id, HttpSession session) throws LogicException {
		MyFile mf = myFileService.getMyFile(id);
		try {
			File file = avatarUploadServer.seekFile(mf.getSeekPath());
			session.setAttribute(AVATAR, new AvatarFile(file.getAbsolutePath(), mf));
		} catch (MyFileNotFoundException e) {
			throw new LogicException(e.getI18nMessage());
		}
		return new Info(true);
	}

	@RequestMapping(value = "my/avatar/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Info confirm(@RequestBody Crop crop, HttpSession session) throws LogicException {
		crop.setFile((File) session.getAttribute(AVATAR));
		userService.updateAvatar(crop);
		return new Info(true);
	}

	@RequestMapping(value = "my/avatar/drew", method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> drew(HttpSession session) throws MyFileNotFoundException {
		File file = (File) session.getAttribute(AVATAR);
		if (file == null || !file.exists()) {
			throw new MyFileNotFoundException();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("image/" + Files.getFileExtension(file)));
		headers.setContentLength(file.length());
		FileSystemResource res = new FileSystemResource(file);
		return new ResponseEntity<FileSystemResource>(res, headers, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping(value = "manage/avatar/deletePhysical", method = RequestMethod.POST)
	public Info delete(@RequestParam("path") String path) throws LogicException{
		System.out.println("delete");
		avatarUploadServer.deleteFile(path);
		return new Info(true);
	}

	public class AvatarFile extends File {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private AvatarFile(String absPath, MyFile file) {
			super(absPath);
			this.myFile = file;
		}

		private MyFile myFile;

		public MyFile getMyFile() {
			return myFile;
		}

	}
}
