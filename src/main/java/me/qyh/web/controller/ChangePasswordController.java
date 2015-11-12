package me.qyh.web.controller;

import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.entity.validator.UserValidator;
import me.qyh.exception.LogicException;
import me.qyh.service.UserService;
import me.qyh.utils.Validators;
import me.qyh.web.Webs;
import me.qyh.web.tag.token.Token;

@Controller
@RequestMapping(value = "my/password")
public class ChangePasswordController extends BaseController {

	private static final Pattern PASSWORD_PATTERN = UserValidator.PASSWORD_PATTERN;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "change", method = RequestMethod.GET)
	public String change() {
		return "my/password/change";
	}

	@Token
	@RequestMapping(value = "change", method = RequestMethod.POST)
	public String change(@RequestParam(value = "oldPassword", defaultValue = "") String oldPassword,
			@RequestParam(value = "newPassword", defaultValue = "") String newPassword, HttpSession session,
			@RequestParam("validateCode") String code, RedirectAttributes ra, ModelMap model) {
		if (!Webs.matchValidateCode(session, code)) {
			model.addAttribute(ERROR, new I18NMessage("error.validateCode"));
			return "my/password/change";
		}
		if (!Validators.validate(PASSWORD_PATTERN, newPassword)) {
			model.addAttribute(ERROR, new I18NMessage("validation.password.invalid"));
			return "my/password/change";
		}

		try {
			userService.changePassword(oldPassword, newPassword);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/password/change";
		}
		ra.addFlashAttribute(SUCCESS, new I18NMessage("page.password.change.success"));
		return "redirect:/";
	}
}
