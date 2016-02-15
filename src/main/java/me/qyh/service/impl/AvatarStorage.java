package me.qyh.service.impl;

import me.qyh.entity.MyFile;
import me.qyh.upload.server.inner.LocalFileStorage;

public class AvatarStorage extends LocalFileStorage {

	@Override
	public boolean canStore(MyFile file) {
		return (file instanceof AvatarFile);
	}
}
