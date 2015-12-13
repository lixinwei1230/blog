package me.qyh.web.controller;

import java.io.File;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import me.qyh.bean.Info;
import me.qyh.config.FileWriteConfig;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.helper.page.SimpleBootstrapPage;
import me.qyh.page.PageType;
import me.qyh.server.UserServer;
import me.qyh.service.WidgetService;
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
	@Autowired
	private WidgetService widgetService;
	
	private static final String USER = "user";
	private static final String WIDGET_PAGE = "_page";


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
	
	@RequestMapping(value = "user/{id}/index", method = RequestMethod.GET)
	public String index(@PathVariable int id, ModelMap model) throws LogicException {
		User user = userServer.getUserById(id);
		model.addAttribute(USER, user);
		if (user.getSpace() != null) {
			return String.format("redirect:/space/%s/index", user.getSpace().getId());
		}
		model.addAttribute(WIDGET_PAGE, new SimpleBootstrapPage(widgetService.getPage(PageType.HOMEPAGE, user)));
		return "user/index";
	}

	@RequestMapping(value = "user/{id}/", method = RequestMethod.GET)
	public String index() throws LogicException {
		return "forward:/index";
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
