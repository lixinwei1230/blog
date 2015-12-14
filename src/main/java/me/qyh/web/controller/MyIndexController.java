package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.qyh.exception.LogicException;
import me.qyh.helper.page.SimpleBootstrapPage;
import me.qyh.page.PageType;
import me.qyh.security.UserContext;
import me.qyh.service.WidgetService;

@Controller
@RequestMapping("my")
public class MyIndexController {

	private static final String WIDGET_PAGE = "_page";

	@Autowired
	private WidgetService widgetService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) throws LogicException {
		model.addAttribute(WIDGET_PAGE,
				new SimpleBootstrapPage(widgetService.getPage(PageType.HOMEPAGE, UserContext.getUser())));
		return "my/index";
	}

}
