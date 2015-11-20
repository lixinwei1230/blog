package me.qyh.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import me.qyh.bean.Info;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.server.UserServer;
import me.qyh.service.impl.AvatarUploadServer;
import me.qyh.utils.Files;
import me.qyh.web.InvalidParamException;
import me.qyh.web.Webs;

@Controller
public class UserController extends BaseController {

	@Autowired
	private UserServer userServer;
	@Value("${config.maxSpaceSize}")
	private int maxSpaceSize;
	@Autowired
	private AvatarUploadServer uploadServer;
	@Autowired
	private Im4javas im4javas;
	@Autowired
	private ConfigServer configServer;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;

	@RequestMapping(value = "user/info", method = RequestMethod.GET, params = { "spaces" })
	@ResponseBody
	public Info getUserInfo(@RequestParam("spaces") Set<String> spaces) {
		if (spaces.isEmpty() || spaces.size() > maxSpaceSize) {
			throw new InvalidParamException();
		}
		return new Info(true, userServer.findUserBySpaces(spaces));
	}

	@RequestMapping(value = "user/info", method = RequestMethod.GET, params = { "ids" })
	@ResponseBody
	public Info getUserInfoByIds(@RequestParam("ids") Set<Integer> ids) {
		if (ids.isEmpty() || ids.size() > maxSpaceSize) {
			throw new InvalidParamException();
		}
		return new Info(true, userServer.findUserByIds(ids));
	}

	@RequestMapping(value = "avatar", method = RequestMethod.GET)
	public void avatar(@RequestParam(value = "path", defaultValue = "") String path,
			@RequestParam(value = "size", required = false) Integer size, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		if(!Webs.isSafeFilePath(path)){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		FileWriteConfig config = configServer.getAvatarWriteConfig();
		RequestMatcher matcher = config.getRequestMatcher();
		if (matcher != null && !matcher.matches(request.getRequest())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		File file = uploadServer.seekFile(path);
		String etag = Webs.generatorETag(path + file.lastModified());
		if (request.checkNotModified(etag)) {
			return;
		}
		response.setContentType(URLConnection.guessContentTypeFromName(file.getName()));
		ImageZoomMatcher zm = config.getZoomMatcher();
		if (zm != null && zm.zoom(size, file)) {
			String relativePath = getRelativePath(file);
			File dest = new File(imageCacheDir + relativePath + File.separator
					+ Files.appendFilename(file.getName(), "_" + file.lastModified() + "_" + size));
			if (dest.exists()) {
				file = dest;
			} else {
				File folder = new File(imageCacheDir, relativePath);
				if (!folder.exists() && !folder.mkdirs()) {
					throw new SystemException(
							String.format("%s:创建文件夹%s失败", this.getClass().getName(), folder.getAbsolutePath()));
				}
				try {
					file = zoomImage(file.getAbsolutePath(), dest.getAbsolutePath(), size, false);
				} catch (Exception e) {
					throw new SystemException(e);
				}
			}
		}
		response.setContentLength((int) file.length());
		try {
			FileUtils.copyFile(file, response.getOutputStream());
		} catch (IOException e) {
		}
	}

	private String getRelativePath(File file) {
		String absPath = file.getParent();
		return absPath.substring(absPath.indexOf(File.separatorChar));
	}

	private File zoomImage(String absPath, String destPath, int size, boolean force) throws Exception {
		if (!force) {
			File zoom = new File(absPath);
			ImageInfo info = im4javas.getImageInfo(absPath);
			if (info.getHeight() < size && info.getWidth() < size) {
				return zoom;
			}
		}
		im4javas.zoom(absPath, destPath, size);
		return new File(destPath);
	}

}
