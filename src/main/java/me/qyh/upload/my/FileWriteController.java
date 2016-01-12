package me.qyh.upload.my;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import me.qyh.bean.Info;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileWriteConfig;
import me.qyh.config.ImageZoomMatcher;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.file.ImageProcessing;
import me.qyh.helper.file.Resize;
import me.qyh.upload.server.inner.LocalFileStorage;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;
import me.qyh.web.SpringContextHolder;
import me.qyh.web.Webs;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class FileWriteController {

	@Autowired
	private ConfigServer configServer;
	@Autowired
	private ImageProcessing im4javas;
	@Autowired
	private MyServerFileMapping fileMapping;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;
	private Collection<LocalFileStorage> stores = null;

	@RequestMapping(value = "static/{storeId}/{y}/{m}/{d}/{name:.+}", method = RequestMethod.GET)
	public void write(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		String pathPrefix = File.separator + y + File.separator + m + File.separator + d + File.separator;
		if (!Webs.isSafeFilePath(pathPrefix)) {
			throw new InvalidParamException();
		}
		LocalFileStorage store = seek(storeId);
		File ff = null;
		ParsedName pn = new ParsedName(name);
		File seek = store.seek(pathPrefix + pn.fullName);
		if (pn.isImage && (pn.size != null)) {
			FileWriteConfig config = configServer.getFileWriteConfig(store);
			ImageZoomMatcher matcher = config.getZoomMatcher();
			// can be zoomed
			File zoom = new File(imageCacheDir, pathPrefix + name);
			if (matcher != null && matcher.zoom(pn.size, seek)) {
				if (!zoom.exists()) {
					synchronized (this) {
						if (!zoom.exists()) {
							Resize resize = new Resize();
							resize.setSize(pn.size);
							try {
								Files.forceMkdir(zoom.getParentFile());
								im4javas.zoom(seek, zoom, resize);
							} catch (Exception e) {
								throw new SystemException(e.getMessage(), e);
							}
						}
					}
				}
			} else {
				zoom = seek;
			}
			ff = zoom;
		} else {
			ff = seek;
		}

		if (!ff.exists()) {
			throw new MyFileNotFoundException();
		}
		try {
			response.sendRedirect(fileMapping.prefix(store) + Strings.cleanPath(pathPrefix) + ff.getName());
			return ;
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = "file/{storeId}/{y}/{m}/{d}/{name}/{ext}/{key}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@PathVariable("storeId") int storeId, @PathVariable("y") String y, @PathVariable("m") String m,
			@PathVariable("d") String d, @PathVariable("name") String name, @PathVariable("ext") String ext,
			@PathVariable("key") String key, ServletWebRequest request, HttpServletResponse response) {
		try {
			String path = File.separator + y + File.separator + m + File.separator + d + File.separator + name + "."
					+ ext;
			LocalFileStorage storage = seek(storeId);
			if (!storage.getKey().equals(key)) {
				return new Info(false);
			}
			try {
				File file = storage.seek(path);
				return new Info(FileUtils.deleteQuietly(file));
			} catch (MyFileNotFoundException e) {
				return new Info(true);
			}
		} catch (InvalidParamException e) {
		}
		return new Info(true);
	}

	private Integer parseSize(String toParse) {
		try {
			return Integer.parseInt(toParse);
		} catch (NumberFormatException e) {
			throw new InvalidParamException();
		}
	}

	private class ParsedName {
		private ParsedName(String name) {
			// 123.jpg 123_200.jpg
			int pos = name.indexOf(".");
			if (pos == -1) {
				throw new InvalidParamException();
			}
			String _name = Files.getFilename(name);
			int sizePos = _name.indexOf("_");
			if (sizePos != -1) {
				String[] parsed = _name.split("_");
				if (parsed.length == 0) {
					throw new InvalidParamException();
				}
				_name = parsed[0];
				if (parsed.length > 1) {
					size = parseSize(parsed[1]);
				}
			}
			isImage = Webs.isWebImage(name);
			ext = Files.getFileExtension(name);
			fullName = _name + "." + ext;
		}

		private boolean isImage;
		private Integer size;
		private String ext;
		private String fullName;
	}

	private LocalFileStorage seek(int id) {
		if (stores == null) {
			stores = SpringContextHolder.getBeansOfType(LocalFileStorage.class).values();
		}
		if (Validators.isEmptyOrNull(stores)) {
			throw new InvalidParamException();
		}
		for (LocalFileStorage storage : stores) {
			if (storage.id() == id) {
				return storage;
			}
		}
		throw new InvalidParamException();
	}
}
