package me.qyh.web.controller;

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
import me.qyh.exception.LogicException;
import me.qyh.helper.page.SimpleBootstrapPage;
import me.qyh.page.LocationWidget;
import me.qyh.page.PageType;
import me.qyh.page.widget.Widget.WidgetType;
import me.qyh.page.widget.config.BindingResultWidgetConfig;
import me.qyh.page.widget.config.WidgetConfigAttribute;
import me.qyh.security.UserContext;
import me.qyh.service.WidgetService;

@Controller
@RequestMapping("my/page")
public class MyPageController extends BaseController {

	private static final String PAGE = "page";

	@Autowired
	private Validator locationWidgetValidator;
	@Autowired
	private Validator configValidatr;
	@Autowired
	private WidgetService widgetService;

	@InitBinder(value = "config")
	protected void initConfigBinder(WebDataBinder binder) {
		binder.setValidator(configValidatr);
	}

	@InitBinder(value = "locationWidget")
	protected void initWidgetBinder(WebDataBinder binder) {
		binder.setValidator(locationWidgetValidator);
	}

	@RequestMapping(value = "design", method = RequestMethod.GET)
	public String design(@RequestParam(value = "type", defaultValue = "HOMEPAGE") PageType type, ModelMap model)
			throws LogicException {
		model.addAttribute(PAGE, new SimpleBootstrapPage(widgetService.getPage(type, UserContext.getUser())));

		switch (type) {
		case HOMEPAGE:
			return "my/page/homepage_design";
		default:
			return "my/page/other_design";
		}
	}

	@RequestMapping(value = "widget/put", method = RequestMethod.POST)
	@ResponseBody
	public Info putWidget(@RequestBody @Validated LocationWidget widget) throws LogicException {
		widgetService.putWidget(widget);
		return new Info(true);
	}

	@RequestMapping(value = "widget/delete", method = RequestMethod.POST)
	@ResponseBody
	public Info deleteWidget(@RequestParam("id") int id) throws LogicException {
		widgetService.removeWidget(id);
		return new Info(true);
	}

	@RequestMapping(value = "widget/list", method = RequestMethod.GET)
	@ResponseBody
	public Info list() {
		return new Info(true, widgetService.findWidgets(UserContext.getUser()));
	}

	@RequestMapping(value = "widget/preview/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Info previewWidget(@RequestParam(value = "type", defaultValue = "SYSTEM") WidgetType type,
			@PathVariable("id") int id) throws LogicException {
		return new Info(true, widgetService.getPreviewWidget(id, type));
	}

	@RequestMapping(value = "widget/{id}/config", method = RequestMethod.GET)
	@ResponseBody
	public Info getWidgetConfig(@PathVariable("id") int id) throws LogicException {
		return new Info(true, widgetService.getConfig(id));
	}

	@RequestMapping(value = "config/update", method = RequestMethod.POST)
	@ResponseBody
	public Info getWidgetConfig(
			@WidgetConfigAttribute(validate = true, requestBody = true) BindingResultWidgetConfig config)
					throws LogicException {
		widgetService.updateConfig(config.getConfig());
		return new Info(true);
	}

	@RequestMapping(value = "widget/update", method = RequestMethod.POST)
	@ResponseBody
	public Info updateWidget(@RequestBody @Validated LocationWidget widget, @RequestParam("wrap") boolean wrap)
			throws LogicException {
		widgetService.updateLocationWidget(widget, wrap);

		return new Info(true);
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index() {
		return "my/page/index";
	}

}
