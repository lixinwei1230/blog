package me.qyh.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.SpaceStatus;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.security.UserContext;
import me.qyh.service.SpaceService;
import me.qyh.web.tag.token.Token;

@Controller
@RequestMapping("open")
public class SpaceOpenController extends BaseController {

	@Autowired
	private Validator spaceValidator;
	@Autowired
	private SpaceService spaceService;

	@InitBinder(value = "space")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(spaceValidator);
	}

	@Token
	@RequestMapping(value = "space", method = RequestMethod.POST)
	public String openSpace(@Validated Space space, BindingResult result,
			ModelMap model, RedirectAttributes ra) {
		User current = UserContext.getUser();
		if (current.hasRole(RoleEnum.ROLE_SPACE)) {
			return "redirect:/";
		}

		if (result.hasErrors()) {
			return "my/space/to_open";
		}

		space.setCreateDate(new Date());
		space.setStatus(SpaceStatus.NORMAL);
		space.setUser(current);

		try {
			spaceService.openSpace(space);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/space/to_open";
		}

		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.space.open"));
		return "redirect:/";
	}

	@RequestMapping(value = "space", method = RequestMethod.GET)
	public String index() {
		User current = UserContext.getUser();
		if (current.hasRole(RoleEnum.ROLE_SPACE)) {
			return "redirect:/";
		}

		return "my/space/to_open";
	}

}
