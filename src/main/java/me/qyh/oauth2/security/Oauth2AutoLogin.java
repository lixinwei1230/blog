package me.qyh.oauth2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * 用于oauth2中认证成功，已经绑定当前用户的情况下的自动登录
 * 
 * @author mhlx
 *
 */
public class Oauth2AutoLogin {

	/**
	 * manager中必须提供一个 {@code Oauth2UserAuthencationProvider}
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * session安全策略，可以防止固化攻击等
	 */
	private SessionAuthenticationStrategy sessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy();

	/**
	 * 自动登录
	 * 
	 * @param principal
	 *            用户的id和oauth类型
	 * @param request
	 *            当前请求
	 * @param response
	 */
	public void autoLogin(OauthPrincipal principal, HttpServletRequest request, HttpServletResponse response) {
		Oauth2UserAuthencationToken token = new Oauth2UserAuthencationToken(principal);
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication auth = authenticationManager.authenticate(token);
		sessionAuthenticationStrategy.onAuthentication(auth, request, response);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void autoLogin(OauthPrincipal principal) {
		Oauth2UserAuthencationToken token = new Oauth2UserAuthencationToken(principal);
		Authentication auth = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
		this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
	}

}
