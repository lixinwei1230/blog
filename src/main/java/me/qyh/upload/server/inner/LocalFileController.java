package me.qyh.upload.server.inner;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.file.BadImageException;
import me.qyh.helper.file.ImageInfo;
import me.qyh.helper.file.ImageProcessing;
import me.qyh.helper.file.Resize;
import me.qyh.utils.Files;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;
import me.qyh.web.SpringContextHolder;
import me.qyh.web.Webs;
import me.qyh.web.controller.BaseController;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("file")
public class LocalFileController extends BaseController  {

	private static final String WEBP_SUPPORT_COOKIE = "WEBP_SUPPORT";
	private static final String WEBP = "webp";
	private static final String WEBP_CONTENT_TYPE = "image/webp";
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	private static final String[] DATE_FORMATS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz",
			"EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
	private static TimeZone GMT = TimeZone.getTimeZone("GMT");

	@Autowired
	protected ConfigServer configServer;
	@Autowired
	private ImageProcessing im4javas;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;
	@Value("${config.file.webpSupport}")
	private boolean supportWebp;
	@Value("${config.file.maxAge}")
	private long maxAge;
	@Value("${config.avatar.filePrefix}")
	private String avatarPrefix;
	
	private Collection<LocalFileStorage> stores = null;
	
	@RequestMapping(value = "{storeId}/{y}/{m}/{d}/{name}/{ext}", method = RequestMethod.GET)
	public void _write(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,
			ServletWebRequest request, HttpServletResponse response)
					throws MyFileNotFoundException {
		this.write(storeId, y, m, d, name, ext, null, request, response);
	}

	@RequestMapping(value = "{storeId}/{y}/{m}/{d}/{name}/{ext}/{size}", method = RequestMethod.GET)
	public void write(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,
			@PathVariable("size") String size, ServletWebRequest request, HttpServletResponse response)
					throws MyFileNotFoundException {
		String path = File.separator + y + File.separator + m + File.separator + d + File.separator + name + "." + ext;
		if (!Webs.isSafeFilePath(path)) {
			throw new InvalidParamException();
		}
		FileWriteConfig config = getFileWriteConfig(name);
		RequestMatcher matcher = config.getRequestMatcher();
		// 防盗链
		if (matcher != null && !matcher.matches(request.getRequest())) {
			throw new MyFileNotFoundException();
		}
		LocalFileStorage store = seek(storeId);
		File seek = store.seek(path);
		boolean isImage = Webs.isWebImage("." + ext);
		if (!isImage) {
			response.setContentLength((int) seek.length());
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment;filename=" + seek.getName());
			try {
				OutputStream out = response.getOutputStream();
				FileUtils.copyFile(seek, out);
			} catch (IOException e) {}
			return;
		}
		if (!isModified(request)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		StringBuilder sb = new StringBuilder(imageCacheDir);
		Resize format = parseSize(size);
		ImageZoomMatcher zm = config.getZoomMatcher();
		boolean zoom = format != null && zm.zoom(format.getSize(), seek) && needZoom(seek, format);
		String _path = path;
		if (zoom) {
			_path = Files.appendFilename(_path,"_", format.getSize());
		}
		sb.append(_path);
		boolean supportWebp = (this.supportWebp && supportWebp(request.getRequest(), path));
		if (supportWebp) {
			sb.append(".").append(WEBP);
		}
		String absPath = sb.toString();
		String etag = Webs.generatorETag(absPath);
		if (request.checkNotModified(etag)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}
		File file = new File(absPath);
		if (!file.exists()) {
			Files.forceMkdir(file.getParentFile());
			response.setContentType(URLConnection.guessContentTypeFromName(path));
			if (!zoom && !supportWebp) {
				file = seek;
			} else {
				synchronized(this){
					if(!file.exists()){
						if (supportWebp) {
							response.setContentType(WEBP_CONTENT_TYPE);
							try {
								im4javas.format(seek, new File(Files.getFilename(absPath)), WEBP);
							} catch (Exception e) {
								throw new SystemException(e);
							}
							seek = file;
						}
						if (zoom) {
							try {
								im4javas.zoom(seek, file, format);
							} catch (Exception e) {
								throw new SystemException(e);
							}
						}
					}
				}
			}
		}
		response.setContentLength((int) file.length());
		response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addDateHeader("Expires", System.currentTimeMillis() + maxAge * 1000);
		response.setHeader("Cache-Control", "max-age=" + maxAge);
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			OutputStream out = response.getOutputStream();
			FileUtils.copyFile(file, out);
		} catch (IOException e) {
		}
	}

	private boolean needZoom(File file, Resize resize) {
		if (!resize.isForce()) {
			int size = resize.getSize();
			try {
				ImageInfo info = im4javas.read(file);
				if (info.getHeight() < size && info.getWidth() < size) {
					return false;
				}
			} catch (BadImageException e) {
				throw new SystemException(e.getMessage(), e);
			}
		}
		return true;
	}

	private boolean supportWebp(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, WEBP_SUPPORT_COOKIE);
		if (cookie != null && "true".equalsIgnoreCase(cookie.getValue())) {
			String ext = Files.getFileExtension(name);
			return "jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext);
		}
		return false;
	}

	private LocalFileStorage seek(int id) {
		if(stores == null){
			stores = SpringContextHolder.getBeansOfType(LocalFileStorage.class).values();
		}
		if(Validators.isEmptyOrNull(stores)){
			throw new InvalidParamException();
		}
		for(LocalFileStorage storage : stores){
			if(storage.id() == id){
				return storage;
			}
		}
		throw new InvalidParamException();
	}

	/**
	 * @see HttpHeaders
	 * @param req
	 * @return
	 */
	private boolean isModified(ServletWebRequest req) {
		String lastModifyTime = req.getHeader(IF_MODIFIED_SINCE);
		if (lastModifyTime != null) {
			for (String dateFormat : DATE_FORMATS) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
				simpleDateFormat.setTimeZone(GMT);
				try {
					long time = simpleDateFormat.parse(lastModifyTime).getTime();
					if(req.checkNotModified(time)){
						return false;
					}
				} catch (ParseException e) {
					// ignore
				}
			}
		}
		return true;
	}

	private Resize parseSize(String _size) {
		if (Validators.isEmptyOrNull(_size, true)) {
			return null;
		} else {
			boolean force = false;
			int pos = _size.indexOf("!");
			if (pos != -1) {
				force = true;
			}
			String strSize = pos == -1 ? _size : _size.substring(0, pos);
			try {
				int size = Integer.parseInt(strSize);
				Resize resize = new Resize();
				resize.setForce(force);
				resize.setSize(size);
				return resize;
			} catch (Exception e) {
				throw new InvalidParamException();
			}
		}
	}
	
	protected FileWriteConfig getFileWriteConfig(String name){
		if(name.startsWith(avatarPrefix)){
			return configServer.getAvatarWriteConfig();
		}
		return configServer.getFileWriteConfig();
	}
	
}
