package me.qyh.upload.server.inner;

import me.qyh.upload.server.UploadedFile;

public class SuccessUploadFile extends UploadedFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long size;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public SuccessUploadFile() {
		super();
	}

	public SuccessUploadFile(String originalFilename) {
		super(originalFilename);
	}

	@Override
	protected boolean getSuccess() {
		return true;
	}

}
