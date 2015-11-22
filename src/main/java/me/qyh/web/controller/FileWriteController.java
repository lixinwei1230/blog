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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.WebUtils;

import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.utils.Files;
import me.qyh.web.Webs;

public abstract class FileWriteController extends BaseController {

	private static final String WEBP_SUPPORT_COOKIE = "WEBP_SUPPORT";
	private static final String WEBP = "webp";
	private static final String WEBP_CONTENT_TYPE = "image/webp";
	@Autowired
	private ConfigServer configServer;
	@Autowired
	private Im4javas im4javas;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;
	
	public void write(String path,Integer size, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		if (!Webs.isSafeFilePath(path)) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		FileWriteConfig config = configServer.getAvatarWriteConfig();
		RequestMatcher matcher = config.getRequestMatcher();
		if (matcher != null && !matcher.matches(request.getRequest())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		File seek = seek(path);
		if (Webs.isWebImage(seek.getName())) {
			response.setContentType(URLConnection.guessContentTypeFromName(seek.getName()));
			String etag = Webs.generatorETag(path);
			if (request.checkNotModified(etag)) {
				return;
			}
			
			String relativePath = getRelativePath(seek);
			File cacheFolder = new File(imageCacheDir + relativePath);
			if(supportWebp(request.getRequest())){
				String absPath = cacheFolder.getAbsolutePath() + File.separator + seek.getName();
				try {
					im4javas.format(WEBP, seek.getAbsolutePath(),absPath);
					seek = new File(absPath + "." + WEBP);
					response.setContentType(WEBP_CONTENT_TYPE);
				} catch (Exception e) {
					throw new SystemException(e);
				}
			}
			
			ImageZoomMatcher zm = config.getZoomMatcher();
			if (zm != null && zm.zoom(size, seek)) {
				File dest = new File(cacheFolder, Files.appendFilename(seek.getName(), "_" + size));
				if (dest.exists()) {
					seek = dest;
				} else {
					File folder = new File(imageCacheDir + relativePath);
					if (!folder.exists() && !folder.mkdirs()) {
						throw new SystemException(
								String.format("%s:创建文件夹%s失败", this.getClass().getName(), folder.getAbsolutePath()));
					}
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

	protected abstract File seek(String path) throws MyFileNotFoundException;

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
	
	protected boolean supportWebp(HttpServletRequest request){
		Cookie cookie = WebUtils.getCookie(request, WEBP_SUPPORT_COOKIE);
		if(cookie != null){
			return "true".equalsIgnoreCase(cookie.getValue());
		}
		return false;
	}

}
