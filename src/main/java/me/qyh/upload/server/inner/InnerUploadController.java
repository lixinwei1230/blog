package me.qyh.upload.server.inner;

import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.bean.Info;
import me.qyh.config.FileWriteConfig;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.upload.server.UploadServer;
import me.qyh.upload.server.UploadedInfo;
import me.qyh.upload.server.UploadedResult;
import me.qyh.web.controller.FileWriteController;

@Controller
public class InnerUploadController extends FileWriteController{

	@Autowired
	private UploadServer innerFileUploadServer;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Info upload(@RequestParam("files") List<MultipartFile> files, Locale locale) throws LogicException {
		return new UploadedInfo( (UploadedResult) innerFileUploadServer.upload(files), messageSource, locale);
	}
	
	@RequestMapping(value = "write", method = RequestMethod.GET)
	public void write(@RequestParam(value = "path", defaultValue = "") String path,
			@RequestParam(required = false, value = "size") Integer size, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		super.write(path, size, request, response);
	}

	@Override
	protected File seek(String path) throws MyFileNotFoundException {
		return innerFileUploadServer.seekFile(path);
	}

	@Override
	protected FileWriteConfig getWriteConfig() {
		return configServer.getFileWriteConfig();
	}

}
