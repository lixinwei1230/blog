package me.qyh.entity;

import java.util.Date;

import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Files;
import me.qyh.web.Webs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	private transient FileStorage store;
	private FileStatus status;
	private String relativePath;//关键字段
	private String originalFilename;// 原始文件名
	private MyFile cover;// 封面 gif图片会用到
	private String filenameWithoutExtension;
	private boolean isCover;
	private Integer width;//宽度(图片)
	private Integer height;//长度(图片)

	public boolean isImage() {
		return Webs.isWebImage(originalFilename);
	}

	public String getOriginalFilenameWithoutExtension() {
		return originalFilename == null ? null : Files.getFilename(originalFilename);
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
	public FileStorage getStore() {
		return store;
	}

	public void setStore(FileStorage store) {
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

	@JsonProperty
	public String getUrl() {
		return store == null ? null : store.fileUrl(this);
	}

	public MyFile() {

	}

	public MyFile(User user, long size, String name, Date uploadDate, String originalFilename) {
		this.user = user;
		this.size = size;
		this.extension = Files.getFileExtension(name);
		this.name = name;
		this.uploadDate = uploadDate;
		this.status = FileStatus.NORMAL;
		this.originalFilename = originalFilename;
		this.filenameWithoutExtension = Files.getFilename(name);
	}

	public MyFile(User user, long size, String name, Date uploadDate, String originalFilename,
			boolean isCover) {
		this(user,size,name,uploadDate,originalFilename);
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

	public String getFilenameWithoutExtension() {
		return filenameWithoutExtension;
	}

	@Override
	public String toString() {
		return "MyFile [user=" + user + ", size=" + size + ", extension=" + extension + ", name=" + name
				+ ", uploadDate=" + uploadDate + ", status=" + status + ", relativePath=" + relativePath
				+ ", originalFilename=" + originalFilename + ", cover=" + cover + ", filenameWithoutExtension="
				+ filenameWithoutExtension + ", isCover=" + isCover + "]";
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
