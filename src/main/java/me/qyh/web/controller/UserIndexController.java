package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.qyh.entity.User;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.helper.page.SimpleBootstrapPage;
import me.qyh.page.PageType;
import me.qyh.security.UserContext;
import me.qyh.server.UserServer;
import me.qyh.service.WidgetService;

@Controller
public class UserIndexController {

	private static final String USER = "user";
	private static final String WIDGET_PAGE = "_page";

	@Autowired
	private WidgetService widgetService;
	@Autowired
	private UserServer userServer;

	@RequestMapping(value = "user/{id}/index", method = RequestMethod.GET)
	public String index(@PathVariable int id, ModelMap model)
			throws LogicException {
		User user = userServer.getUserById(id);
		model.addAttribute(USER, user);
		if (user.getSpace() != null) {
			return String.format("redirect:/space/%s/index",
					user.getSpace().getId());
		}
		model.addAttribute(WIDGET_PAGE, new SimpleBootstrapPage(
				widgetService.getPage(PageType.HOMEPAGE, user)));
		return "user/index";
	}

	@RequestMapping(value = "user/{id}/", method = RequestMethod.GET)
	public String index() throws DataNotFoundException {
		return "forward:/index";
	}

	@RequestMapping(value = "my/index", method = RequestMethod.GET)
	public String index(ModelMap model) throws LogicException {
		model.addAttribute(WIDGET_PAGE, new SimpleBootstrapPage(widgetService
				.getPage(PageType.HOMEPAGE, UserContext.getUser())));
		return "my/index";
	}

}
