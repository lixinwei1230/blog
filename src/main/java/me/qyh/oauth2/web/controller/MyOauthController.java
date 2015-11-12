package me.qyh.oauth2.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.service.OauthService;
import me.qyh.security.UserContext;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;
import me.qyh.web.controller.BaseController;

@Controller
@RequestMapping("my/oauth")
public class MyOauthController extends BaseController {

	private static final String OAUTHS = "oauths";

	@Autowired
	private OauthService oauthService;

	@RequestMapping(value = "unbind", method = RequestMethod.POST)
	@ResponseBody
	public Info unbind(@RequestParam(value = "type", defaultValue = "QQ") OauthType type) throws LogicException {
		oauthService.unbind(UserContext.getUser(), OauthType.QQ);
		return new Info(true);
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute(OAUTHS, oauthService.findOauthBinds(UserContext.getUser()));
		return "my/oauth/list";
	}

	@RequestMapping(value = "completeInfo", method = RequestMethod.GET)
	public String sendCompleteInfoEmail() {
		return "my/oauth/competeinfo_email";
	}

	@RequestMapping(value = "completeInfo", method = RequestMethod.GET, params = { "code", "userid" })
	public String checkCompleteInfoEmail(@RequestParam(value = "code", defaultValue = "") String code, int userid,
			RedirectAttributes ra) {
		if (code.trim().isEmpty() || userid == 0) {
			throw new InvalidParamException();
		}
		try {
			oauthService.checkCompleteInfoEmail(code, userid);
		} catch (LogicException e) {
			ra.addFlashAttribute(ERROR, e.getI18nMessage());
			return "redirect:completeInfo";
		}
		ra.addFlashAttribute("code", code);
		ra.addFlashAttribute("userid", userid);
		return "my/oauth/competeinfo_email";
	}

	@RequestMapping(value = "completeInfo", method = RequestMethod.POST, params = { "email" })
	public String sendCompleteInfoMail(@RequestParam(value = "email", defaultValue = "") String email, ModelMap model,
			RedirectAttributes ra) {
		if (!Validators.validateEmail(email)) {
			model.addAttribute(ERROR, new I18NMessage("validation.email.invalid"));
			return "my/oauth/competeinfo_email";
		}
		try {
			oauthService.sendCompleteInfoEmail(email, UserContext.getUser());
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			return "my/oauth/competeinfo_email";
		}
		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.oauth.completeUserInfo.sendEmail"));
		return "redirect:completeInfo";
	}

}
