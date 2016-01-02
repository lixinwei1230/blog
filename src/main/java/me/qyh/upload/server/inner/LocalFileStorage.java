package me.qyh.upload.server.inner;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.entity.MyFile;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.upload.server.FileMapper;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

public class LocalFileStorage implements FileStorage, InitializingBean {

	private String absPath;
	private int id;
	private FileMapping mapping = new DefaultMapping();
	private String key;
	private StoreType type = StoreType.ALL;

	@Autowired
	private UrlHelper urlHelper;

	@Override
	public String store(FileMapper mapper) throws Exception {
		File mappered = mapper.getMappered();
		if (!mappered.exists()) {
			throw new SystemException(String.format("文件%s不存在", mappered.getAbsolutePath()));
		}
		String path = Files.ymd();
		File dir = new File(absPath, path);
		Files.forceMkdir(dir);
		FileUtils.copyFile(mappered, new File(dir, mappered.getName()));
		return path;
	}

	@Override
	public String delUrl(MyFile file) {
		StringBuilder sb = new StringBuilder();
		sb.append(urlHelper.getUrl());
		sb.append("/file/");
		sb.append(id);
		sb.append(Strings.cleanPath(file.getRelativePath()));
		sb.append(Files.getFilename(file.getName())).append("/");
		sb.append(Files.getFileExtension(file.getName())).append("/");
		sb.append(key).append("/delete");
		return sb.toString();
	}

	@Override
	public String fileUrl(MyFile file) {
		return mapping.mapping(file, this);
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public boolean canStore(MyFile file) {
		switch (type) {
		case IMAGE_ONLY:
			return Webs.isWebImage(file.getName());
		case ALL:
			return true;
		default : return false;
		}
	}

	public File seek(String path) throws MyFileNotFoundException {
		File file = new File(absPath, path);
		if (!file.exists()) {
			throw new MyFileNotFoundException();
		}
		return file;
	}

	private final class DefaultMapping implements FileMapping {

		@Override
		public String mapping(MyFile file, FileStorage store) {
			StringBuilder sb = new StringBuilder();
			sb.append(urlHelper.getUrl());
			sb.append("/file/");
			sb.append(id);
			sb.append(Strings.cleanPath(file.getRelativePath()));
			sb.append(Files.getFilename(file.getName())).append("/");
			sb.append(Files.getFileExtension(file.getName()));
			return sb.toString();
		}

	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FileMapping getMapping() {
		return mapping;
	}

	public void setMapping(FileMapping mapping) {
		this.mapping = mapping;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.key = Strings.getMd5(Strings.getMd5(this.getClass().getName() + "-" + id));
	}
	
	public void setType(StoreType type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public enum StoreType{
		IMAGE_ONLY,ALL
	}
}
