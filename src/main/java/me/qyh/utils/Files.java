package me.qyh.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

import me.qyh.exception.SystemException;

public final class Files {

	public static String getFileExtension(String filename) {
		if (filename.indexOf('.') != -1) {
			return filename.substring(filename.lastIndexOf('.') + 1);
		}
		return "";
	}

	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static String getFilename(String filename) {
		if (filename.indexOf('.') != -1) {
			return filename.substring(0, filename.lastIndexOf('.'));
		}
		return filename;
	}

	/**
	 * 
	 * @return yyyy/MM/dd
	 */
	public static String ymd() {
		Calendar cal = Calendar.getInstance();
		StringBuilder sb = new StringBuilder(File.separator);
		sb.append(cal.get(Calendar.YEAR));
		sb.append(File.separator);
		sb.append((cal.get(Calendar.MONTH) + 1));
		sb.append(File.separator);
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append(File.separator);
		return sb.toString();
	}

	public static String appendFilename(String name, String append) {
		String filename = getFilename(name);
		String extension = getFileExtension(name);
		return filename + append + (Validators.isEmptyOrNull(extension, true) ? "" : "." + extension);
	}

	public static void forceMkdir(File dir) {
		try {
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
}
