package me.qyh.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.WebUtils;

import me.qyh.bean.Info;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStorage;
import me.qyh.upload.server.inner.LocalFileStorage;
import me.qyh.utils.Files;
import me.qyh.web.InvalidParamException;
import me.qyh.web.Webs;

@Controller
@RequestMapping("file")
public class LocalFileController extends BaseController {

	private static final String WEBP_SUPPORT_COOKIE = "WEBP_SUPPORT";
	private static final String WEBP = "webp";
	private static final String WEBP_CONTENT_TYPE = "image/webp";
	@Autowired
	private ConfigServer configServer;
	@Autowired
	private Im4javas im4javas;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;
	@Autowired
	private LocalFileStorage avatarStore;
	@Autowired
	private FileServer fileServer;

	@RequestMapping(value = "{storeId}/{y}/{m}/{d}/{name}/{ext}/{size}", method = RequestMethod.GET)
	public void write(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,
			@PathVariable("size") Integer size, ServletWebRequest request, HttpServletResponse response)
					throws MyFileNotFoundException {
		String path = File.separator + y + File.separator + m + File.separator + d + File.separator + name + "." + ext;
		if (!Webs.isSafeFilePath(path)) {
			throw new MyFileNotFoundException();
		}
		LocalFileStorage store = seek(storeId);
		FileWriteConfig config = configServer.getFileWriteConfig(store);
		RequestMatcher matcher = config.getRequestMatcher();
		if (matcher != null && !matcher.matches(request.getRequest())) {
			throw new MyFileNotFoundException();
		}
		File seek = store.seek(path);
		if (Webs.isWebImage(seek.getName())) {
			response.setContentType(URLConnection.guessContentTypeFromName(seek.getName()));
			String etag = Webs.generatorETag(path + seek.lastModified());
			if (request.checkNotModified(etag)) {
				return;
			}
			String relativePath = getRelativePath(seek);
			File cacheFolder = new File(imageCacheDir + relativePath);
			Files.forceMkdir(cacheFolder);
			if (supportWebp(request.getRequest(), seek)) {
				response.setContentType(WEBP_CONTENT_TYPE);
				String absPath = cacheFolder.getAbsolutePath() + File.separator
						+ Files.appendFilename(seek.getName(), seek.lastModified() + "");
				File webp = new File(absPath + "." + WEBP);
				if (!webp.exists()) {
					try {
						im4javas.format(WEBP, seek.getAbsolutePath(), absPath);
					} catch (Exception e) {
						throw new SystemException(e);
					}
				}
				seek = webp;
			}

			ImageZoomMatcher zm = config.getZoomMatcher();
			if (zm != null && zm.zoom(size, seek)) {
				File dest = new File(cacheFolder,
						Files.appendFilename(seek.getName(), "_" + seek.lastModified() + "_" + size));
				if (dest.exists()) {
					seek = dest;
				} else {
					try {
						seek = zoomImage(seek.getAbsolutePath(), dest.getAbsolutePath(), size, false);
					} catch (Exception e) {
						throw new SystemException(e);
					}
				}
			}
		} else {
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment;filename=" + seek.getName());
		}
		response.setContentLength((int) seek.length());
		try {
			OutputStream out = response.getOutputStream();
			FileUtils.copyFile(seek, out);
		} catch (IOException e) {
		}
	}
	
	@RequestMapping(value = "{storeId}/{y}/{m}/{d}/{name}/{ext}", method = RequestMethod.GET)
	public void write(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,
			ServletWebRequest request, HttpServletResponse response)
					throws MyFileNotFoundException {
		this.write(storeId, y, m, d, name, ext,null, request, response);
	}
	
	@RequestMapping(value = "{storeId}/{y}/{m}/{d}/{name}/{ext}/{key}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,@PathVariable("key") String key,
			ServletWebRequest request, HttpServletResponse response) {
		try{
			String path = File.separator + y + File.separator + m + File.separator + d + File.separator + name + "." + ext;
			LocalFileStorage storage = seek(storeId);
			try {
				File file = storage.seek(path);
				return new Info(FileUtils.deleteQuietly(file));
			} catch (MyFileNotFoundException e) {
				return new Info(true);
			}
		}catch(InvalidParamException e){
		}
		return new Info(true);
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

	private boolean supportWebp(HttpServletRequest request, File file) {
		Cookie cookie = WebUtils.getCookie(request, WEBP_SUPPORT_COOKIE);
		if (cookie != null && "true".equalsIgnoreCase(cookie.getValue())) {
			String ext = Files.getFileExtension(file);
			return "jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext);
		}
		return false;
	}

	private LocalFileStorage seek(int id) {
		LocalFileStorage storage = null;
		if (id == avatarStore.id()) {
			storage = avatarStore;
		}
		if (storage == null) {
			FileStorage seeked = fileServer.getStore(id);
			if (seeked != null && (seeked instanceof LocalFileStorage)) {
				storage = (LocalFileStorage) seeked;
			}
		}
		if (storage == null) {
			throw new InvalidParamException();
		}
		return storage;
	}
}
