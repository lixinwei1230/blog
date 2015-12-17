package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.exception.Oauth2ConnectionException;
import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.exception.Oauth2InvalidStateException;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.oauth2.service.OauthService;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class OauthAuthorizeController extends BaseOauthController {

	@Autowired
	private OauthService oauthService;

	/**
	 * 检查state值是否匹配，防止csrf
	 * 
	 * @param request
	 * @throws Oauth2InvalidStateException
	 */
	private void checkState(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean valid = false;
		if (session != null) {
			String state = (String) session.getAttribute(STATE);
			session.removeAttribute(STATE);
			String _state = request.getParameter(STATE);
			valid = !(Validators.isEmptyOrNull(_state, true) || !_state.equals(state));
		}
		if (!valid) {
			throw new Oauth2InvalidStateException(oauthType(), "oauth2 server传来的state无法被找到或者匹配失败，无法完成认证");
		}
	}
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws Oauth2ConnectionException {
		String state = Strings.uuid();
		HttpSession session = request.getSession();
		session.setAttribute(STATE, state);
		return "redirect:" + oauth2().getAuthorizeUrl(state);
	}
	
	@RequestMapping(value = "success")
	public String success(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws Oauth2Exception, LogicException {
		String errorCode = request.getParameter("error");
		if (errorCode != null) {
			throw new Oauth2Exception(oauthType(),String.format("返回redire_uri时包含错误code%s", errorCode));
		}

		checkState(request);

		String code = request.getParameter("code");
		if (Validators.isEmptyOrNull(code, true)) {
			throw new InvalidParamException();
		}
		OauthPrincipal principal = oauth2().getOauthPrincipal(code);
		
		User user = oauthService.get(principal);
		if (user == null) {
			OauthUser _user = oauth2().queryUserInfo(principal);
			putInfo(request.getSession(), _user, principal);
			return "redirect:/oauth/bind/index";
		}

		autoLogin(principal, request, response);
		return null;
	}
	
	private void putInfo(HttpSession session, OauthUser user, OauthPrincipal principal) {
		String securityKey = Strings.uuid();
		session.setAttribute(SECRET_KEY, securityKey);
		session.setAttribute(securityKey, principal);
		session.setAttribute(OAUTH_USER, user);
	}
	
	abstract OauthType oauthType();
	
	abstract Oauth2 oauth2();

}
