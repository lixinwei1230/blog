package me.qyh.service.impl;

import java.util.Date;

import me.qyh.entity.MyFile;
import me.qyh.entity.User;

public class AvatarFile extends MyFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AvatarFile(User user, long size, String name, Date uploadDate, String originalFilename) {
		super(user, size, name, uploadDate, originalFilename);
	}
}