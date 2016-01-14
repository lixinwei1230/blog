package me.qyh.upload.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.exception.SystemException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;

public class UploadedInfo extends Info {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;
	private List<UploadedFile> files = new ArrayList<UploadedFile>();

	public UploadedInfo(UploadedResult result, MessageSource messageSource, Locale locale) {
		I18NMessage error = result.getError();
		if (error != null) {
			this.error = messageSource.getMessage(error.getCode(), error.getParams(), locale);
		}
		for (UploadedFile file : result.getFiles()) {
			_UploadedFile _file = new _UploadedFile(messageSource, locale);
			try {
				BeanUtils.copyProperties(file, _file);
			} catch (BeansException e) {
				throw new SystemException(e);
			}
			files.add(_file);
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

	class _UploadedFile extends UploadedFile {

		private MessageSource messageSource;
		private Locale locale;

		public _UploadedFile(MessageSource messageSource, Locale locale) {
			this.messageSource = messageSource;
			this.locale = locale;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public String getErrorMessage() {
			I18NMessage error = getError();
			return error == null ? null : messageSource.getMessage(error.getCode(), error.getParams(), locale);
		}

	}
}
