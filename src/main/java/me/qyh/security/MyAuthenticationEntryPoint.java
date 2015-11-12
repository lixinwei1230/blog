package me.qyh.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	@Autowired
	private UrlHelper urlHelper;

	public MyAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (Webs.isAjaxRequest(request)) {
			response.sendError(403);
		} else {
			super.commence(request, response, exception);
		}
	}

	@Override
	protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		return request.getScheme() + "://" + urlHelper.getUrl() + getLoginFormUrl();
	}

}
