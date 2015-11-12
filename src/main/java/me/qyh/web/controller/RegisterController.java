package me.qyh.web.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.service.UserService;
import me.qyh.utils.Validators;
import me.qyh.web.Webs;
import me.qyh.web.tag.token.Token;

@Controller
public class RegisterController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private Validator userValidator;

	@InitBinder(value = "user")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@Token
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(@Validated User user, BindingResult result,
			@RequestParam(value = "validateCode") String code,
			HttpSession session, ModelMap model, RedirectAttributes ra) {
		if (!Webs.matchValidateCode(session, code)) {
			model.addAttribute(ERROR, new I18NMessage("error.validateCode"));
			return "register";
		}
		if (result.hasErrors()) {
			return "register";
		}
		try {
			user.setRegisterDate(new Date());
			userService.register(user);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "register";
		}
		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.register"));
		return "redirect:/";
	}

	@RequestMapping(value = "activate", method = RequestMethod.GET)
	public String activate(
			@RequestParam(value = "activateCode",
					defaultValue = "") String activateCode,
			@RequestParam(value = "userid") int userId, ModelMap model,
			RedirectAttributes redirectAttributes) {
		try {
			userService.activate(userId, activateCode);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "reactivate";
		}
		redirectAttributes.addFlashAttribute(SUCCESS,
				new I18NMessage("success.activate"));
		return "redirect:/";
	}

	@RequestMapping(value = "reactivate", method = RequestMethod.GET)
	public String reactivate() {
		return "reactivate";
	}

	@Token
	@RequestMapping(value = "reactivate", method = RequestMethod.POST)
	public String reactivate(
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "validateCode") String code,
			RedirectAttributes ra, HttpSession session, ModelMap model) {
		if (!Webs.matchValidateCode(session, code)) {
			model.addAttribute(ERROR, new I18NMessage("error.validateCode"));
			return "reactivate";
		}
		if (!Validators.validateEmail(email)) {
			model.addAttribute(ERROR,
					new I18NMessage("validation.email.invalid"));
			return "reactivate";
		}
		try {
			userService.reactive(name, email);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "reactivate";
		}
		ra.addFlashAttribute(SUCCESS,
				new I18NMessage("page.reactivate.success"));
		return "redirect:/reactivate";
	}

}
