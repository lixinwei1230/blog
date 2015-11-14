package me.qyh.entity;

import java.io.File;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.qyh.upload.server.FileStore;
import me.qyh.utils.Files;
import me.qyh.web.Webs;

/**
 * 文件对象
 * 
 * @author mhlx
 * 
 */
public class MyFile extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private long size;
	private String extension;
	private String name;
	private Date uploadDate;
	private transient FileStore store;
	private FileStatus status;
	private String relativePath;
	private String originalFilename;// 原始文件名
	private MyFile cover;// 封面 gif图片会用到
	private boolean isCover;

	public boolean isImage() {
		return Webs.isWebImage(originalFilename);
	}

	public String getOriginalFilenameWithoutExtension() {

		return originalFilename == null ? "" : Files.getFilename(originalFilename);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	@JsonIgnore
	public FileStore getStore() {
		return store;
	}

	public void setStore(FileStore store) {
		this.store = store;
	}

	public FileStatus getStatus() {
		return status;
	}

	public void setStatus(FileStatus status) {
		this.status = status;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getSeekPath() {
		if (relativePath == null || name == null) {
			return null;
		}
		return relativePath + File.separator + name;
	}

	public String getSeekPrefixUrl() {
		return store == null ? null : store.seekPrefix();
	}

	public MyFile() {

	}

	public MyFile(User user, long size, String extension, String name, Date uploadDate, FileStore store,
			FileStatus status, String relativePath, String originalFilename, boolean isCover) {
		super();
		this.user = user;
		this.size = size;
		this.extension = extension;
		this.name = name;
		this.uploadDate = uploadDate;
		this.store = store;
		this.status = status;
		this.relativePath = relativePath;
		this.originalFilename = originalFilename;
		this.isCover = isCover;
	}

	public MyFile getCover() {
		return cover;
	}

	public void setCover(MyFile cover) {
		this.cover = cover;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public boolean getIsCover() {
		return isCover;
	}

	public void setIsCover(boolean isCover) {
		this.isCover = isCover;
	}
}
