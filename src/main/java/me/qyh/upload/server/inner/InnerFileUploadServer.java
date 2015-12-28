package me.qyh.upload.server.inner;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import me.qyh.bean.I18NMessage;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileUploadConfig;
import me.qyh.config.FileUploadConfig.SizeLimit;
import me.qyh.config.FileUploadConfig.SizeLimit.Result;
import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.security.UserContext;
import me.qyh.upload.server.UploadServer;
import me.qyh.upload.server.UploadedResult;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class InnerFileUploadServer implements UploadServer {

	@Autowired
	private ConfigServer configServer;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private InnerFileStore innerFileStore;
	@Value("${config.file.absPath}")
	private String absPath;
	@Value("${config.tempdir}")
	private String tempDir;
	@Autowired
	private Im4javas im4javas;

	private static final String IMAGEPREFIX = "image/";
	private static final String GIF = "gif";
	private static final String JPEG = "jpeg";
	private static final String PNG = "png";

	/**
	 * 如果不在同一系统的话 首先需要通过 FileServer 获取要上传的服务器的上传地址。 然后用户将文件post到该地址。
	 * 服务器接收到文件后跟主服务器做交互，判断文件大小等信息。 校验通过之后将保存的信息传给主服务器，主服务器保存
	 * 
	 * @param files
	 */
	@Override
	public UploadedResult upload(List<MultipartFile> files) {
		User user = UserContext.getUser();
		UploadedResult info = new UploadedResult(true);
		long totalSize = calculateTotalSize(files);
		long allFileSize = fileDao.selectAllFileSize(UserContext.getUser());

		FileUploadConfig config = configServer.getFileUploadConfig(user);

		if (allFileSize + totalSize > config.getMaxSizeOfUser()) {
			info.setError(new I18NMessage("error.upload.user.oversize", config.getMaxSizeOfUser(),
					allFileSize + totalSize - config.getMaxSizeOfUser()));
			return info;
		}

		for (MultipartFile file : files) {
			String originalFilename = file.getOriginalFilename();
			if (file.getSize() == 0) {
				info.addError(originalFilename, new I18NMessage("error.upload.emptyFile"));
				continue;
			}
			boolean image = maybeImage(file.getContentType());
			FileUploadConfig._Config _config = image ? config.getImageConfig() : config.getConfig();
			SizeLimit sl = _config.getSizeLimit();
			if (sl != null) {
				Result result = sl.allow(file);
				if (!result.isAllow()) {
					info.addError(originalFilename,
							new I18NMessage("error.upload.singlefile.oversize", result.getMaxAllowSize()));
					continue;
				}
			}
			String extension = Files.getFileExtension(file.getOriginalFilename());

			if (!image && !Strings.inArray(extension, _config.getAllowFileTypes(), true)) {
				info.addError(originalFilename, new I18NMessage("error.upload.invalidExtension", extension));
				continue;
			}

			String newFilename = Strings.uuid() + "." + extension;
			String relativePath = Files.ymd();

			File temp = new File(tempDir + relativePath);
			if (!temp.exists() && !temp.mkdirs()) {
				throw new SystemException(this.getClass().getName() + ":无法创建目录:" + temp.getPath());
			}
			File _file = new File(temp, newFilename);
			try {
				file.transferTo(_file);
			} catch (Exception e1) {
				throw new SystemException(e1);
			}
			String contentType = file.getContentType();
			if (image) {
				try {
					ImageInfo ii = im4javas.getImageInfo(_file.getAbsolutePath());
					if (!Strings.inArray(ii.getType(), _config.getAllowFileTypes(), true)) {
						info.addError(originalFilename, new I18NMessage("error.upload.invalidExtension", ii.getType()));
						continue;
					}
					FileUploadConfig._ImageConfig imConfig = (FileUploadConfig._ImageConfig) _config;
					if (ii.getWidth() > imConfig.getMaxWidth()) {
						info.addError(originalFilename,
								new I18NMessage("error.upload.image.width.invalid", imConfig.getMaxWidth()));
						continue;
					}
					if (ii.getHeight() > imConfig.getMaxHeight()) {
						info.addError(originalFilename,
								new I18NMessage("error.upload.image.height.invalid", imConfig.getMaxWidth()));
						continue;
					}
					File rename = new File(_file.getParent(),
							Files.getFilename(_file.getName()) + "." + ii.getType().toLowerCase());
					if (!_file.renameTo(rename)) {
						throw new SystemException(
								String.format("将文件%s修改为%s后缀失败", _file.getAbsolutePath(), ii.getType().toLowerCase()));
					}
					_file = rename;
					if (!GIF.equalsIgnoreCase(ii.getType())) {
						try {
							im4javas.strip(_file.getAbsolutePath());
						} catch (Exception e) {
							throw new SystemException(e.getMessage(), e);
						}
					}
					contentType = IMAGEPREFIX + ii.getType().toLowerCase();
					if (GIF.equalsIgnoreCase(ii.getType())) {
						try {
							int numImages = Im4javas.getNumImages(_file);
							// 只有1帧
							if (numImages == 1) {
								try {
									im4javas.format(PNG, _file.getAbsolutePath(), _file.getAbsolutePath());
								} catch (Exception e) {
									throw new SystemException(e.getMessage(), e);
								}
								_file =  new File(_file.getParent(), _file.getName() + "." + PNG);
								contentType = IMAGEPREFIX + PNG;
							}
						} catch (IOException e) {
							throw new SystemException(e.getMessage(), e);
						}
					}
				} catch (BadImageException e) {
					info.addError(originalFilename, new I18NMessage("error.upload.badImage"));
					continue;
				}
			}
			File _cover = createCover(temp, _file, contentType);
			boolean hasCover = (_cover != null);
			Date now = new Date();
			MyFile mf = new MyFile(user, _file.length(), Files.getFileExtension(_file.getName()), _file.getName(), now,
					innerFileStore, FileStatus.NORMAL, relativePath, file.getOriginalFilename(), false);
			if(hasCover){
				MyFile cover = new MyFile(user, _cover.length(), JPEG, _cover.getName(), now, innerFileStore, FileStatus.NORMAL,
						relativePath, file.getOriginalFilename(), true);
				fileDao.insert(cover);
				mf.setCover(cover);
			}
			fileDao.insert(mf);

			info.addSuccess(originalFilename, _file.length());
			
			File folder = new File(absPath + relativePath);
			if (!folder.exists() && !folder.mkdirs()) {
				throw new SystemException(this.getClass().getName() + ":无法创建目录:" + folder.getPath());
			}
			try{
				FileUtils.copyFile(_file, new File(folder,_file.getName()));
				if(hasCover){
					FileUtils.copyFile(_cover, new File(folder,_cover.getName()));
				}
			}catch (IOException e) {
				throw new SystemException(e.getMessage(), e);
			}
		}
		return info;
	}

	@Override
	public Object deleteFile(String... paths) throws LogicException {
		if (paths != null && paths.length > 0) {
			for (String path : paths) {
				File file = new File(absPath + path);
				if (file.exists() && file.canExecute()) {
					FileUtils.deleteQuietly(file);
				}
			}
		}
		return null;
	}

	@Override
	public File seekFile(String relativePath) throws MyFileNotFoundException {
		File file = new File(absPath + relativePath);
		if (!file.exists() || !file.isFile()) {
			throw new MyFileNotFoundException();
		}
		return file;
	}

	private boolean maybeImage(String contentType) {
		return contentType != null && Strings.startsWithIgnoreCase(contentType, IMAGEPREFIX);
	}

	private File createCover(File dir,File toCreate,String contentType){
		boolean needCreate = (contentType != null && Strings.endsWithIgnoreCase(contentType, GIF));
		File cover = null;
		if(needCreate){
			String coverName = Files.getFilename(toCreate.getName()) + "." + JPEG;
			cover = new File(dir, coverName);
			try {
				im4javas.writeFirstFrameOfGif(toCreate.getAbsolutePath(), cover.getAbsolutePath());
			} catch (Exception e) {
				throw new SystemException(e);
			}
		}
		return cover;
	}
	
	private long calculateTotalSize(List<MultipartFile> files) {
		long total = 0;
		for (MultipartFile file : files) {
			total += file.getSize();
		}
		return total;
	}
}
