package me.qyh.upload.server;

import java.io.File;
import java.util.Date;

import me.qyh.entity.MyFile;
import me.qyh.entity.User;

public class FileMapper extends MyFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File mappered;

	public FileMapper() {
		super();
	}

	public FileMapper(User user, long size, String name, Date uploadDate, 
			String originalFilename, File mappered) {
		super(user, size, name, uploadDate, originalFilename);
		this.mappered = mappered;
	}

	public File getMappered() {
		return mappered;
	}

	public void setMappered(File mappered) {
		this.mappered = mappered;
	}

}
