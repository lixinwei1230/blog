package me.qyh.helper.jqueryfileupload;

public class FailedUploadFile extends UploadedFile {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	protected boolean getSuccess() {
		return false;
	}

}