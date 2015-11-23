package me.qyh.web.controller;

import java.io.File;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import me.qyh.bean.Info;
import me.qyh.config.FileWriteConfig;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.server.UserServer;
import me.qyh.service.impl.AvatarUploadServer;
import me.qyh.web.InvalidParamException;

@Controller
public class UserController extends FileWriteController {

	@Autowired
	private UserServer userServer;
	@Value("${config.maxSpaceSize}")
	private int maxSpaceSize;
	@Autowired
	private AvatarUploadServer uploadServer;

	@RequestMapping(value = "user/info", method = RequestMethod.GET, params = { "spaces" })
	@ResponseBody
	public Info getUserInfo(@RequestParam("spaces") Set<String> spaces) {
		if (spaces.isEmpty() || spaces.size() > maxSpaceSize) {
			throw new InvalidParamException();
		}
		return new Info(true, userServer.findUserBySpaces(spaces));
	}

	@RequestMapping(value = "user/info", method = RequestMethod.GET, params = { "ids" })
	@ResponseBody
	public Info getUserInfoByIds(@RequestParam("ids") Set<Integer> ids) {
		if (ids.isEmpty() || ids.size() > maxSpaceSize) {
			throw new InvalidParamException();
		}
		return new Info(true, userServer.findUserByIds(ids));
	}

	@RequestMapping(value = "avatar", method = RequestMethod.GET)
	public void avatar(@RequestParam(value = "path", defaultValue = "") String path,
			@RequestParam(value = "size", required = false) Integer size, ServletWebRequest request,
			HttpServletResponse response) throws MyFileNotFoundException {
		super.write(path, size, request, response);
	}

	@Override
	protected File seek(String path) throws MyFileNotFoundException {
		return uploadServer.seekFile(path);
	}

	@Override
	protected FileWriteConfig getWriteConfig() {
		return configServer.getAvatarWriteConfig();
	}
}
