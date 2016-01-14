package me.qyh.upload.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.qyh.bean.I18NMessage;

public class UploadedFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;
	@JsonIgnore
	private I18NMessage error;
	private long size;
	private String url;
	private String originalFilename;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public I18NMessage getError() {
		return error;
	}

	public void setError(I18NMessage error) {
		this.error = error;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public UploadedFile(String originalFilename, long size, String url) {
		super();
		this.success = true;
		this.size = size;
		this.url = url;
		this.originalFilename = originalFilename;
	}

	public UploadedFile(String originalFilename, I18NMessage error) {
		this.success = false;
		this.error = error;
		this.originalFilename = originalFilename;
	}

	public UploadedFile() {

	}

}
