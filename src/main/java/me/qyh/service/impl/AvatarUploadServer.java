package me.qyh.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.config.ConfigServer;
import me.qyh.config.FileUploadConfig.SizeLimit;
import me.qyh.config.FileUploadConfig.SizeLimit.Result;
import me.qyh.config.FileUploadConfig._ImageConfig;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.security.UserContext;
import me.qyh.upload.server.inner.BadImageException;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;

@Service
public class AvatarUploadServer {

	@Autowired
	private ConfigServer configServer;
	@Autowired
	private Im4javas im4javas;
	@Value("${config.avatar.absPath}")
	private String absPath;
	@Value("${config.tempdir}")
	private String tempdir;

	public File upload(MultipartFile file) throws LogicException {
		if (file.isEmpty()) {
			throw new LogicException("error.upload.emptyFile");
		}
		_ImageConfig config = configServer.getAvatarConfig(UserContext.getUser());
		SizeLimit sl = config.getSizeLimit();
		if (sl != null) {
			Result result = sl.allow(file);
			if (!result.isAllow()) {
				throw new LogicException("error.upload.singlefile.oversize", new Object[] { result.getMaxAllowSize() });
			}
		}
		String extension = Files.getFileExtension(file.getOriginalFilename());
		File folder = new File(tempdir, Files.ymd());
		if (!folder.exists() && !folder.mkdirs()) {
			throw new SystemException(
					String.format("%s:目录%s不存在并且创建失败", this.getClass().getName(), folder.getAbsolutePath()));
		}
		File dest = new File(folder, Strings.uuid() + "." + extension);
		try {
			file.transferTo(dest);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		ImageInfo info = null;
		try {
			info = im4javas.getImageInfo(dest.getAbsolutePath());
		} catch (BadImageException e) {
			throw new LogicException("error.upload.badImage");
		}
		if (!Strings.inArray(info.getType(), config.getAllowFileTypes(), true)) {
			throw new LogicException("error.upload.invalidExtension", new Object[] { info.getType() });
		}
		if (info.getWidth() > config.getMaxWidth()) {
			throw new LogicException("error.upload.image.width.invalid", config.getMaxWidth());
		}
		if (info.getHeight() > config.getMaxHeight()) {
			throw new LogicException("error.upload.image.height.invalid", config.getMaxHeight());
		}
		File rename = new File(dest.getParent(),
				Files.getFilename(dest.getName()) + "." + info.getType().toLowerCase());
		if (!dest.renameTo(rename)) {
			throw new SystemException(
					String.format("将文件%s修改为%s后缀失败", dest.getAbsolutePath(), info.getType().toLowerCase()));
		}
		return rename;
	}

	public File seekFile(String relativePath) throws MyFileNotFoundException {
		File file = new File(absPath + relativePath);
		if (!file.isFile()) {
			throw new MyFileNotFoundException();
		}
		return file;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}
}
