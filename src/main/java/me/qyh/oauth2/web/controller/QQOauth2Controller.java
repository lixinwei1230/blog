package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.qyh.bean.I18NMessage;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.exception.Oauth2ConnectionException;
import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.qq.AccessToken;
import me.qyh.oauth2.qq.OpenId;
import me.qyh.oauth2.qq.QQOauth2;
import me.qyh.oauth2.security.Oauth2AutoLogin;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.oauth2.service.OauthService;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;

@Controller
@RequestMapping("oauth2/qq")
public class QQOauth2Controller extends BaseOauthController {

	@Autowired
	private QQOauth2 qqOauth2;
	@Autowired
	private OauthService oauthService;
	@Autowired
	private Oauth2AutoLogin autoLogin;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws Oauth2ConnectionException {
		String state = Strings.uuid();
		HttpSession session = request.getSession();
		session.setAttribute(STATE, state);
		return "redirect:" + qqOauth2.getAuthorizeUrl(state, request);
	}

	@RequestMapping(value = "success")
	public String success(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws Oauth2Exception, LogicException {
		String errorCode = request.getParameter("error");
		if (errorCode != null) {
			throw new Oauth2Exception(OauthType.QQ, String.format("返回redire_uri时包含错误code%s", errorCode));
		}

		checkState(request, OauthType.QQ);

		String code = request.getParameter("code");
		if (Validators.isEmptyOrNull(code, true)) {
			throw new InvalidParamException();
		}
		AccessToken token = qqOauth2.getAccessToken(code);
		OpenId openId = qqOauth2.getOpenId(token);
		OauthPrincipal principal = new OauthPrincipal(openId.getOpenId(), OauthType.QQ);
		principal.setToken(token);

		User user = oauthService.get(principal);
		if (user == null) {
			OauthUser _user = qqOauth2.queryUserInfo(principal);
			putInfo(request.getSession(), _user, principal);
			return "redirect:/oauth/bind/index";
		}

		// 已经绑定账号则自动登录
		autoLogin.autoLogin(principal, request, response);
		ra.addFlashAttribute(SUCCESS, new I18NMessage("success.oauth", user.getNickname()));

		return "redirect:/";
	}

	protected void putInfo(HttpSession session, OauthUser user, OauthPrincipal principal) {
		String securityKey = Strings.uuid();
		session.setAttribute(SECRET_KEY, securityKey);
		session.setAttribute(securityKey, principal);
		session.setAttribute(OAUTH_USER, user);
	}

}
