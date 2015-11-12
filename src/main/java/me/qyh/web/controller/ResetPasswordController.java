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
@RequestMapping("password")
public class ResetPasswordController extends BaseController {

	private static final Pattern PASSWORD_PATTERN = UserValidator.PASSWORD_PATTERN;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "forget", method = RequestMethod.GET)
	public String forget() {
		return "my/password/forget";
	}

	@Token
	@RequestMapping(value = "forget", method = RequestMethod.POST)
	public String forget(
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "validateCode") String code,
			HttpSession session, ModelMap model, RedirectAttributes ra) {
		if (!Webs.matchValidateCode(session, code)) {
			model.addAttribute(ERROR, new I18NMessage("error.validateCode"));
			return "my/password/forget";
		}
		if (!Validators.validateEmail(email)) {
			model.addAttribute(ERROR,
					new I18NMessage("validation.email.invalid"));
			return "my/password/forget";
		}
		try {
			userService.forgetPassword(name, email);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/password/forget";
		}
		ra.addFlashAttribute(SUCCESS,
				new I18NMessage("success.forgetPassword"));
		return "redirect:/";
	}

	@RequestMapping(value = "reset", method = RequestMethod.GET,
			params = { "code", "userid" })
	public String reset(
			@RequestParam(value = "code", defaultValue = "") String code,
			@RequestParam(value = "userid") int userId, ModelMap model,
			RedirectAttributes ra) {
		try {
			userService.resetPasswordCheck(code, userId);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/password/forget";
		}
		ra.addFlashAttribute("userId", userId);
		ra.addFlashAttribute("code", code);
		return "redirect:/password/reset";
	}

	@RequestMapping(value = "reset", method = RequestMethod.GET)
	public String reset() {
		return "my/password/reset";
	}

	@Token
	@RequestMapping(value = "reset", method = RequestMethod.POST)
	public String reset(
			@RequestParam(value = "newPassword",
					defaultValue = "") String newPassword,
			@RequestParam(value = "userId") int userId,
			@RequestParam(value = "code", defaultValue = "") String code,
			HttpSession session, ModelMap model, RedirectAttributes ra) {
		if (!Validators.validate(PASSWORD_PATTERN, newPassword)) {
			model.addAttribute("userId", userId);
			model.addAttribute("code", code);
			model.addAttribute(ERROR,
					new I18NMessage("validation.password.invalid"));
			return "my/password/reset";
		}
		try {
			userService.resetPassword(code, newPassword, userId);
		} catch (LogicException e) {
			model.addAttribute("userId", userId);
			model.addAttribute("code", code);
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/password/forget";
		}
		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.resetPassword"));
		return "redirect:/";
	}

}
