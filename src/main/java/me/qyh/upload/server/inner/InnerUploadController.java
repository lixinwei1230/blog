package me.qyh.upload.server.inner;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.bean.Info;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.upload.server.UploadServer;
import me.qyh.upload.server.UploadedInfo;
import me.qyh.upload.server.UploadedResult;
import me.qyh.utils.Files;
import me.qyh.web.Webs;

@Controller
public class InnerUploadController {

	@Autowired
	private UploadServer innerFileUploadServer;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Im4javas im4javas;
	@Autowired
	private ConfigServer configServer;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Info upload(@RequestParam("files") List<MultipartFile> files, Locale locale) throws LogicException {
		return new UploadedInfo( (UploadedResult) innerFileUploadServer.upload(files), messageSource, locale);
	}
	
	@RequestMapping(value = "write", method = RequestMethod.GET)
	public void write(@RequestParam(value = "path", defaultValue = "") String path,
			@RequestParam(required = false, value = "size") Integer size, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		FileWriteConfig config = configServer.getFileWriteConfig();
		RequestMatcher matcher = config.getRequestMatcher();
		if (matcher != null && !matcher.matches(request.getRequest())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		File seek = innerFileUploadServer.seekFile(path);
		if (Webs.isWebImage(seek.getName())) {
			response.setContentType(URLConnection.guessContentTypeFromName(seek.getName()));
			String etag = Webs.generatorETag(path);
			if (request.checkNotModified(etag)) {
				return;
			}
			ImageZoomMatcher zm = config.getZoomMatcher();
			if (zm != null && zm.zoom(size, seek)) {
				String relativePath = getRelativePath(seek);
				File dest = new File(imageCacheDir + relativePath + File.separator
						+ Files.appendFilename(seek.getName(), "_" + size));
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
