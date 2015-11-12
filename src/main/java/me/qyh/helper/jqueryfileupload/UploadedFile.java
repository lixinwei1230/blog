package me.qyh.helper.jqueryfileupload;

public abstract class UploadedFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String originalFilename;

	public String getOriginalFilename() {
		return originalFilename;
	}

	public UploadedFile(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public UploadedFile() {

	}

	protected abstract boolean getSuccess();

}
