package me.qyh.helper.jqueryfileupload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.qyh.bean.I18NMessage;

public class UploadedResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;
	private List<UploadedFile> files = new ArrayList<UploadedFile>();
	private I18NMessage error;

	public void addError(String name, I18NMessage error) {
		I18NFailedUploadFile file = new I18NFailedUploadFile(name);
		file.setError(error);
		files.add(file);
	}

	public void addSuccess(String name, long size) {
		SuccessUploadFile file = new SuccessUploadFile(name);
		file.setSize(size);
		files.add(file);
	}

	public List<UploadedFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

	public UploadedResult() {
		super();
	}

	public UploadedResult(boolean success) {
		this.success = success;
	}

	public I18NMessage getError() {
		return error;
	}

	public void setError(I18NMessage error) {
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
