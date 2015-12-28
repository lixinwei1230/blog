package me.qyh.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.bean.Info;
import me.qyh.web.Webs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * 登录失败处理器
 *
 */
public class LoginFailureHandler extends DefaultLoginFailureHandler {

	@Autowired
	private ObjectWriter objectWriter;
	@Autowired
	private MessageSource messageSource;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String msg = messageSource.getMessage("error.loginFailed", new Object[] {}, request.getLocale());
		if (Webs.isAjaxRequest(request)) {
			Webs.writeInfo(response, objectWriter, new Info(false, msg));
		} else {
			request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, msg);
			response.sendRedirect(urlHelper.getUrl() + defaultFailureUrl);
		}
	}
}
