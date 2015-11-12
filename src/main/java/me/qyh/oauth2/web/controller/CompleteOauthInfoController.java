package me.qyh.oauth2.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.service.OauthService;
import me.qyh.security.UserContext;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;
import me.qyh.web.controller.BaseController;
import me.qyh.web.tag.token.Token;

@Controller
public class CompleteOauthInfoController extends BaseController {

	@Autowired
	private OauthService oauthService;
	@Autowired
	private Validator userValidator;
	@Autowired
	private MessageSource messageSource;

	@InitBinder(value = "user")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@RequestMapping(value = "my/oauth/completeEmail",
			method = RequestMethod.GET)
	public String sendCompleteInfoEmail() {
		return "my/oauth/complete_email";
	}

	@RequestMapping(value = "my/oauth/authorizeEmail",
			method = RequestMethod.POST)
	@ResponseBody
	public Info sendAuenthorizeEmail(
			@RequestParam(value = "email", defaultValue = "") String email,
			Locale locale) throws LogicException {
		if (!Validators.validateEmail(email)) {
			return new Info(false, messageSource.getMessage(
					"validation.email.invalid", new Object[] {}, locale));
		}
		oauthService.sendAuthorizeEmail(email, UserContext.getUser());
		return new Info(true);
	}

	@Token
	@RequestMapping(value = "my/oauth/completeEmail",
			method = RequestMethod.POST, params = { "email" })
	public String sendCompleteInfoMail(
			@RequestParam(value = "code", defaultValue = "") String code,
			ModelMap model, RedirectAttributes ra) {
		if (code.isEmpty()) {
			throw new InvalidParamException();
		}
		try {
			oauthService.sendCompleteInfoEmail(code, UserContext.getUser());
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/oauth/complete_email";
		}
		ra.addFlashAttribute(SUCCESS,
				new I18NMessage("success.oauth.completeUserInfo.sendEmail"));
		return "redirect:completeEmail";
	}

	@RequestMapping(value = "oauth/completeInfo", method = RequestMethod.GET)
	public String completeInfo() {
		return "my/oauth/complete_info";
	}

	@RequestMapping(value = "oauth/completeInfo", method = RequestMethod.GET,
			params = { "code", "userid" })
	public String checkCompleteInfoEmail(
			@RequestParam(value = "code", defaultValue = "") String code,
			int userid, RedirectAttributes ra) {
		validate(code, userid);
		try {
			oauthService.checkCompleteInfoEmail(code, userid);
		} catch (LogicException e) {
			ra.addFlashAttribute(ERROR, e.getI18nMessage());
			return "redirect:/my/oauth/completeEmail";
		}
		ra.addFlashAttribute("code", code);
		ra.addFlashAttribute("userid", userid);
		return "redirect:/oauth/completeInfo";
	}

	// 这里为了节省验证篇幅，在前台为用户赋予mail@example.com的邮件
	@Token
	@RequestMapping(value = "oauth/completeInfo", method = RequestMethod.POST)
	public String completeInfo(@Validated User user, BindingResult result,
			@RequestParam("userid") int userid,
			@RequestParam(value = "code", defaultValue = "") String code,
			ModelMap model, RedirectAttributes ra) {
		validate(code, userid);
		if (result.hasErrors()) {
			model.addAttribute("code", code);
			model.addAttribute("userid", userid);
			return "my/oauth/complete_info";
		}
		try {
			oauthService.completeInfo(user, userid, code);
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			model.addAttribute("code", code);
			model.addAttribute("userid", userid);
			return "my/oauth/complete_info";
		}
		return "redirect:/";
	}

	private void validate(String code, int userid) {
		if (code.trim().isEmpty() || userid == 0) {
			throw new InvalidParamException();
		}
	}
}
