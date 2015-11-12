package me.qyh.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectWriter;

import me.qyh.bean.Info;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

/**
 * 登录失败处理器
 * 
 * @author henry.qian
 *
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private String defaultFailureUrl;
	@Autowired
	private UrlHelper urlHelper;
	@Autowired
	private ObjectWriter objectWriter;
	@Autowired
	private MessageSource messageSource;

	public LoginFailureHandler(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
					throws IOException, ServletException {
		String msg = messageSource.getMessage("error.loginFailed",
				new Object[] {}, request.getLocale());
		if (Webs.isAjaxRequest(request)) {
			Webs.writeInfo(response, objectWriter, new Info(false, msg));
		}
		request.getSession()
				.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, msg);
		response.sendRedirect(request.getScheme() + "://" + urlHelper.getUrl()
				+ defaultFailureUrl);
	}
}
