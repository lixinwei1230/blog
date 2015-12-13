package me.qyh.oauth2.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import me.qyh.exception.SystemException;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.exception.Oauth2InvalidStateException;
import me.qyh.oauth2.security.Oauth2AutoLogin;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.security.LoginSuccessHandler;
import me.qyh.utils.Validators;
import me.qyh.web.controller.BaseController;

public class BaseOauthController extends BaseController {

	protected static final String SECRET_KEY = "secretKey";
	protected static final String OAUTH_USER = "oauthUser";
	protected static final String STATE = "state";
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private Oauth2AutoLogin autoLogin;

	/**
	 * 检查state值是否匹配，防止csrf
	 * 
	 * @param request
	 * @throws Oauth2InvalidStateException
	 */
	protected void checkState(HttpServletRequest request, OauthType type) {
		HttpSession session = request.getSession(false);
		boolean valid = false;
		if (session != null) {
			String state = (String) session.getAttribute(STATE);
			session.removeAttribute(STATE);
			String _state = request.getParameter(STATE);
			valid = !(Validators.isEmptyOrNull(_state, true) || !_state.equals(state));
		}
		if (!valid) {
			throw new Oauth2InvalidStateException(type, "oauth2 server传来的state无法被找到或者匹配失败，无法完成认证");
		}
	}

	protected void autoLogin(OauthPrincipal principal, HttpServletRequest request, HttpServletResponse response) {
		// 已经绑定账号则自动登录
		Authentication auth = autoLogin.autoLogin(principal, request, response);
		try {
			loginSuccessHandler.onAuthenticationSuccess(request, response, auth);
		} catch (IOException | ServletException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

}
