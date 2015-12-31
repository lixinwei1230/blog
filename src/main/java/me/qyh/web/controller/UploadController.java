package me.qyh.web.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.qyh.service.UploadService;
import me.qyh.upload.server.UploadedInfo;

@Controller
public class UploadController extends BaseController {

	@Autowired
	private UploadService uploadService;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "upload" , method = RequestMethod.POST)
	@ResponseBody
	public UploadedInfo upload(@RequestParam("files") List<MultipartFile> files,Locale locale){
		return new UploadedInfo(uploadService.upload(files), messageSource, locale);
	}

}
