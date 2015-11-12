package me.qyh.upload.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.upload.server.inner.FailedUploadFile;
import me.qyh.upload.server.inner.I18NFailedUploadFile;

import org.springframework.context.MessageSource;

public class UploadedInfo extends Info {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;

	private List<UploadedFile> files = new ArrayList<UploadedFile>();

	public UploadedInfo(UploadedResult result, MessageSource messageSource,
			Locale locale) {
		I18NMessage error = result.getError();
		if (error != null) {
			this.error = messageSource.getMessage(error.getCode(),
					error.getParams(), locale);
		}
		for (UploadedFile file : result.getFiles()) {
			if (file.getSuccess()) {
				files.add(file);
			} else {
				I18NFailedUploadFile _file = (I18NFailedUploadFile) file;
				I18NMessage _error = _file.getError();
				FailedUploadFile ff = new FailedUploadFile();
				if (_error != null) {
					ff.setError(messageSource.getMessage(_error.getCode(),
							_error.getParams(), locale));
				}
				files.add(ff);
			}
		}
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<UploadedFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}
}
