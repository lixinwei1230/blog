package me.qyh.service.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import me.qyh.upload.server.UploadServer;
import me.qyh.upload.server.inner.BadImageException;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;

@Service
public class AvatarUploadServer implements UploadServer{

	@Autowired
	private ConfigServer configServer;
	@Autowired
	private Im4javas im4javas;
	@Value("${config.avatar.absPath}")
	private String absPath;
	@Value("${config.tempdir}")
	private String tempdir;

	@Override
	public File upload(List<MultipartFile> files) throws LogicException {
		MultipartFile file = files.get(0);
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
		try {
			im4javas.strip(rename.getAbsolutePath());
		} catch (Exception e) {
			throw new SystemException(e.getMessage(),e);
		}
		return rename;
	}

	@Override
	public File seekFile(String relativePath) throws MyFileNotFoundException {
		File file = new File(absPath + relativePath);
		if (!file.exists() || !file.isFile()) {
			throw new MyFileNotFoundException();
		}
		return file;
	}
	
	@Override
	public Object deleteFile(String... paths) throws LogicException {
		if(paths != null && paths.length > 0){
			for(String path : paths){
				File file = new File(absPath + path);
				if(file.exists() && file.canExecute()){
					FileUtils.deleteQuietly(file);
				}
			}
		}
		return null;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}
}
