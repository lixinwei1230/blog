package me.qyh.upload.server.inner;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.bean.Info;
import me.qyh.entity.MyFile;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;
import me.qyh.web.tag.url.UrlHelper;

public class LocalFileStorage implements FileStorage {

	private String absPath;
	private int id;
	private FileMapping mapping = new DefaultMapping();

	@Autowired
	private UrlHelper urlHelper;

	@Override
	public String store(MyFile my,File file) throws Exception {
		if (!file.exists()) {
			throw new SystemException(String.format("文件%s不存在", file.getAbsolutePath()));
		}
		String path = Files.ymd();
		File dir = new File(absPath, path);
		Files.forceMkdir(dir);
		FileUtils.copyFile(file, new File(dir, file.getName()));
		return path;
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
		return !(file instanceof me.qyh.service.impl.AvatarFile);
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
	public Info del(MyFile file) {
		File f = new File(absPath, file.getRelativePath() + File.separator + file.getName());
		boolean flag = false;
		if (!f.exists()) {
			flag = true;
		} else {
			try {
				flag = f.delete();
			} catch (Exception e) {
				flag = false;
			}
		}
		return new Info(flag);
	}

}
