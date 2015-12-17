package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.exception.SystemException;
import me.qyh.oauth2.security.Oauth2AutoLogin;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.security.LoginSuccessHandler;
import me.qyh.web.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

public class BaseOauthController extends BaseController{
	
	protected static final String SECRET_KEY = "secretKey";
	protected static final String OAUTH_USER = "oauthUser";
	protected static final String STATE = "state";
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private Oauth2AutoLogin autoLogin;
	
	protected void autoLogin(OauthPrincipal principal, HttpServletRequest request, HttpServletResponse response) {
		// 已经绑定账号则自动登录
		Authentication auth = autoLogin.autoLogin(principal, request, response);
		try {
			loginSuccessHandler.onAuthenticationSuccess(request, response, auth);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

}
