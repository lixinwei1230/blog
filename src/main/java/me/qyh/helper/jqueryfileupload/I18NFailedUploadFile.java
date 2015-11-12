package me.qyh.helper.jqueryfileupload;

import me.qyh.bean.I18NMessage;

public class I18NFailedUploadFile extends UploadedFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private I18NMessage error;

	public I18NMessage getError() {
		return error;
	}

	public void setError(I18NMessage error) {
		this.error = error;
	}

	public I18NFailedUploadFile() {
		super();
	}

	public I18NFailedUploadFile(String originalFilename) {
		super(originalFilename);
	}

	@Override
	protected boolean getSuccess() {
		return false;
	}
}
