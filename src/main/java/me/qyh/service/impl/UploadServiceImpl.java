package me.qyh.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.bean.I18NMessage;
import me.qyh.config.ConfigServer;
import me.qyh.config.FileUploadConfig;
import me.qyh.config.FileUploadConfig.SizeLimit;
import me.qyh.config.FileUploadConfig._ImageConfig;
import me.qyh.config.FileUploadConfig.SizeLimit.Result;
import me.qyh.dao.FileDao;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.im4java.Im4javas;
import me.qyh.helper.im4java.Im4javas.ImageInfo;
import me.qyh.security.UserContext;
import me.qyh.service.UploadService;
import me.qyh.upload.server.FileMapper;
import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStorage;
import me.qyh.upload.server.UploadedResult;
import me.qyh.upload.server.inner.BadImageException;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private ConfigServer configServer;
	@Autowired
	private FileDao fileDao;
	@Value("${config.file.absPath}")
	private String absPath;
	@Value("${config.tempdir}")
	private String tempDir;
	@Autowired
	private Im4javas im4javas;
	@Autowired
	private FileServer fileServer;

	private static final String IMAGEPREFIX = "image/";
	private static final String GIF = "gif";
	private static final String JPEG = "jpeg";
	private static final String PNG = "png";

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
			Files.forceMkdir(temp);
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
								_file = new File(_file.getParent(), _file.getName() + "." + PNG);
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
			FileStorage storage = fileServer.getStore();
			FileMapper mf = new FileMapper(user, _file.length(), _file.getName(), now, storage,
					file.getOriginalFilename(), _file);
			if (hasCover) {
				FileMapper cover = new FileMapper(user, _cover.length(), _cover.getName(), now, storage,
						file.getOriginalFilename(), _cover);
				cover.setIsCover(true);
				try {
					cover.setRelativePath(storage.store(cover));
				} catch (Exception e) {
					throw new SystemException(e.getMessage(), e);
				}
				fileDao.insert(cover);
				mf.setCover(cover);
			}
			try {
				mf.setRelativePath(storage.store(mf));
			} catch (Exception e) {
				throw new SystemException(e.getMessage(), e);
			}
			fileDao.insert(mf);
			info.addSuccess(originalFilename, _file.length());
		}
		return info;
	}

	@Override
	public File uploadAvatar(MultipartFile file) throws LogicException {
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
		File folder = new File(tempDir, Files.ymd());
		try {
			FileUtils.forceMkdir(folder);
		} catch (IOException e) {
			throw new SystemException(e.getMessage() , e);
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
			throw new SystemException(e.getMessage(), e);
		}
		return rename;
	}

	private boolean maybeImage(String contentType) {
		return contentType != null && Strings.startsWithIgnoreCase(contentType, IMAGEPREFIX);
	}

	private File createCover(File dir, File toCreate, String contentType) {
		boolean needCreate = (contentType != null && Strings.endsWithIgnoreCase(contentType, GIF));
		File cover = null;
		if (needCreate) {
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