package me.qyh.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import me.qyh.exception.SystemException;

public final class Files {

	public static String getFileExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}
	
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}

	public static String getFilename(String filename) {
		int lastPos = filename.lastIndexOf(".");
		if (lastPos != -1) {
			return filename.substring(0, lastPos);
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
	
	public static String prependFilename(String name,Object ... appends){
		if(Validators.isEmptyOrNull(appends)){
			return name;
		}
		String extension = getFileExtension(name);
		StringBuilder sb = new StringBuilder();
		for(Object append : appends){
			sb.append(append);
		}
		sb.append(getFilename(name));
		sb.append((Validators.isEmptyOrNull(extension, true) ? "" : "." + extension));
		return sb.toString();
	}

	public static String appendFilename(String name, Object ... appends) {
		if(Validators.isEmptyOrNull(appends)){
			return name;
		}
		String extension = getFileExtension(name);
		StringBuilder sb = new StringBuilder();
		sb.append(getFilename(name));
		for(Object append : appends){
			sb.append(append);
		}
		sb.append((Validators.isEmptyOrNull(extension, true) ? "" : "." + extension));
		return sb.toString();
	}

	public static void forceMkdir(File dir) {
		try {
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
}
