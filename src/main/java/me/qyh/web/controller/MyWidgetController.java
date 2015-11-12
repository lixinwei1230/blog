package me.qyh.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.page.widget.UserWidget;
import me.qyh.security.UserContext;
import me.qyh.service.WidgetService;

@Controller
@RequestMapping("my/widget")
public class MyWidgetController extends BaseController {

	private static final String WIDGETS = "widgets";

	@Autowired
	private WidgetService widgetService;
	@Autowired
	private Validator userWidgetValidator;

	@InitBinder(value = "userWidget")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userWidgetValidator);
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute(WIDGETS, widgetService.findUserWidgets(UserContext.getUser()));
		return "my/page/widget/index";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam("id") int id) throws LogicException {
		widgetService.deleteUserWidget(id);
		return new Info(true);
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Info add(@RequestBody @Validated UserWidget widget) throws LogicException {
		widget.setCreateDate(new Date());
		widget.setUser(UserContext.getUser());
		widgetService.insertUserWidget(widget);

		return new Info(true);
	}

	@RequestMapping(value = "get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Info get(@PathVariable("id") int id) throws DataNotFoundException {
		return new Info(true, widgetService.getUserWidget(id));
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Info update(@RequestBody @Validated UserWidget widget) throws LogicException {
		widgetService.updateUserWidget(widget);

		return new Info(true);
	}

}
