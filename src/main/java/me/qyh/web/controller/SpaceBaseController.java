package me.qyh.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import me.qyh.entity.Space;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.helper.page.SimpleBootstrapPage;
import me.qyh.page.PageType;
import me.qyh.server.SpaceServer;
import me.qyh.server.UserServer;
import me.qyh.service.WidgetService;
import me.qyh.utils.Validators;
import me.qyh.web.Webs;

@Controller
public class SpaceBaseController extends BaseController {

	private static final String WIDGET_PAGE = "_page";

	private final Logger logger = LoggerFactory.getLogger("errorLogger");

	@Autowired
	protected SpaceServer spaceServer;
	@Autowired
	protected UserServer userServer;
	@Autowired
	private WidgetService widgetService;

	@ModelAttribute
	public final void publicData(@PathVariable("spaceId") String spaceId,
			ModelMap model, HttpServletRequest request) throws LogicException {
		if (Webs.isAjaxRequest(request)) {
			return;
		}
		Space space = spaceServer.getSpaceById(spaceId);
		model.addAttribute(SPACE, space);
		model.addAttribute(USER,
				userServer.getUserById(space.getUser().getId()));

		PageType type = this.providePageType();
		if (type != null) {
			try {
				model.addAttribute(WIDGET_PAGE, new SimpleBootstrapPage(
						widgetService.getPage(type, space.getUser())));
			} catch (DataNotFoundException e) {
				logger.error("空间:{}，找不到对应的自定义页面：{}", space, type);
			}
		}

		Map<String, Object> datas = publicData(space);
		if (!Validators.isEmptyOrNull(datas)) {
			model.addAllAttributes(datas);
		}
	}

	protected final Space getSpace(ModelMap model) {
		return (Space) model.get(SPACE);
	}

	protected Map<String, Object> publicData(Space space) {
		return null;
	}

	protected PageType providePageType() {
		return null;
	}

}
