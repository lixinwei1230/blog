package me.qyh.web.controller;

import java.io.File;

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
import me.qyh.service.UploadService;
import me.qyh.service.UserService;
import me.qyh.upload.server.inner.LocalFileStorage;
import me.qyh.utils.Files;

@Controller
@RequestMapping("my/avatar")
public class MyAvatarController extends BaseController {

	private static final String AVATAR = "avatar";
	private static final String STORE = "store";

	@Autowired
	private UploadService uploadService;
	@Autowired
	private UserService userService;
	@Autowired
	private LocalFileStorage avatarStore;
	@Autowired
	private MyFileService myFileService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute(STORE, avatarStore.id());
		return "my/avatar/index";
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Info upload(@RequestParam(value = "file") MultipartFile file, HttpSession session) throws LogicException {
		session.setAttribute(AVATAR, uploadService.uploadAvatar(file));
		return new Info(true);
	}

	@RequestMapping(value = "choose", method = RequestMethod.GET)
	@ResponseBody
	public Info choose(@RequestParam(value = "id") int id, HttpSession session) throws LogicException {
		MyFile mf = myFileService.getMyFile(id);
		try {
			File file = avatarStore.seek(mf.getRelativePath()+mf.getName());
			session.setAttribute(AVATAR, new AvatarFile(file.getAbsolutePath(), mf));
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
