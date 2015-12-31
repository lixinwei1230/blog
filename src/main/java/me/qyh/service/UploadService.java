package me.qyh.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import me.qyh.exception.LogicException;
import me.qyh.upload.server.UploadedResult;

public interface UploadService {

	UploadedResult upload(List<MultipartFile> files);

	File uploadAvatar(MultipartFile file) throws LogicException;
	

}
