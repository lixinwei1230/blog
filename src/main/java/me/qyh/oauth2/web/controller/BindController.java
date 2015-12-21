package me.qyh.oauth2.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.Oauth2Provider;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.oauth2.service.OauthService;
import me.qyh.utils.Validators;
import me.qyh.web.tag.token.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("oauth/bind")
public class BindController extends BaseOauthController {

	@Autowired
	private OauthService oauthService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Oauth2Provider provider;

	@RequestMapping("index")
	public String index(HttpServletRequest request, ModelMap model, RedirectAttributes ra) {
		HttpSession session = request.getSession(false);
		OauthPrincipal principal = getPrincipal(session, null);
		if (principal == null) {
			ra.addFlashAttribute(ERROR, new I18NMessage("error.oauth.needPrincipal"));
			return "redirect:/login";
		}
		return "oauth_bind";
	}

	@Token
	@RequestMapping(value = "auto", method = RequestMethod.POST)
	public String autoBind(@RequestParam(value = "secretKey", defaultValue = "") String secretKey,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response) throws LogicException {
		HttpSession session = request.getSession(false);
		OauthPrincipal principal = getPrincipal(session, secretKey);
		if (principal == null) {
			ra.addFlashAttribute(ERROR, new I18NMessage("error.oauth.needPrincipal"));
			return "redirect:/login";
		}
		oauthService.autoBind(principal, (OauthUser) session.getAttribute(OAUTH_USER));
		return "redirect:"+provider.seekOauth2(principal.getType()).callBackUrl();
	}

	@Token
	@RequestMapping(value = "specified", method = RequestMethod.POST)
	public String bind(@RequestParam(value = "secretKey", defaultValue = "") String secretKey,
			@RequestParam(value = "email", defaultValue = "") String email,
			@RequestParam(value = "code", defaultValue = "") String code, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes ra, ModelMap model) {
		HttpSession session = request.getSession(false);
		OauthPrincipal principal = getPrincipal(session, secretKey);
		if (principal == null) {
			ra.addFlashAttribute(ERROR, new I18NMessage("error.oauth.needPrincipal"));
			return "redirect:/login";
		}
		try {
			oauthService.bind(principal, code, email, (OauthUser) session.getAttribute(OAUTH_USER));
			return "redirect:"+provider.seekOauth2(principal.getType()).callBackUrl();
		} catch (LogicException e) {
			model.addAttribute(ERROR, e.getI18nMessage());
			model.addAttribute(SECRET_KEY, secretKey);
			return "oauth_bind";
		}
	}

	@ResponseBody
	@RequestMapping(value = "sendCode", method = RequestMethod.POST)
	public Info sendBindEmail(@RequestParam(value = "secretKey", defaultValue = "") String secretKey,
			@RequestParam(value = "email", defaultValue = "") String email, HttpServletRequest request, Locale locale)
					throws LogicException {
		if (!Validators.validateEmail(email)) {
			return new Info(false, messageSource.getMessage("validation.email.invalid", null, locale));
		}
		HttpSession session = request.getSession(false);
		OauthPrincipal principal = getPrincipal(session, secretKey);
		if (principal == null) {
			throw new LogicException("error.oauth.needPrincipal");
		}

		oauthService.sendBindEmail(principal, email);
		return new Info(true);
	}

	private OauthPrincipal getPrincipal(HttpSession session, String key) {
		if (session == null) {
			return null;
		} else {
			String _key = (key == null) ? (String) session.getAttribute(SECRET_KEY) : key;
			return (OauthPrincipal) session.getAttribute(_key);
		}
	}
}
