package me.qyh.web.controller;

import java.io.File;

import javax.servlet.http.HttpSession;

import me.qyh.bean.Crop;
import me.qyh.bean.Info;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.service.UserService;
import me.qyh.service.impl.AvatarUploadServer;
import me.qyh.upload.server.inner.InnerFileStore;
import me.qyh.utils.Files;

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

@Controller
@RequestMapping(value = "my/avatar")
public class MyAvatarController extends BaseController {

	private static final String AVATAR = "avatar";
	private static final String UPLOAD_URL = "uploadUrl";
	private static final String STORE = "store";

	@Autowired
	private AvatarUploadServer uploadServer;
	@Autowired
	private InnerFileStore avatarStore;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute(STORE, avatarStore.id());
		model.addAttribute(UPLOAD_URL, avatarStore.uploadUrl());
		return "my/avatar/index";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Info upload(@RequestParam(value = "file") MultipartFile file, HttpSession session) throws LogicException {
		session.setAttribute(AVATAR, uploadServer.upload(file));
		return new Info(true);
	}

	@RequestMapping(value = "choose", method = RequestMethod.GET)
	@ResponseBody
	public Info choose(@RequestParam(value = "path" ,defaultValue = "") String path,HttpSession session)
			throws LogicException {
		try {
			File file = uploadServer.seekFile(path);
			session.setAttribute(AVATAR, file);
		} catch (MyFileNotFoundException e) {
			throw new LogicException(e.getI18nMessage());
		}
		return new Info(true);
	}

	@RequestMapping(value = "confirm", method = RequestMethod.POST)
	@ResponseBody
	public Info confirm(@RequestBody Crop crop, HttpSession session) throws LogicException {
		crop.setFile((File) session.getAttribute(AVATAR));
		userService.updateAvatar(crop);
		return new Info(true);
	}

	@RequestMapping(value = "drew", method = RequestMethod.GET)
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
}
