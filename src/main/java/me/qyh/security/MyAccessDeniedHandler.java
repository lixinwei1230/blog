package me.qyh.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

import me.qyh.bean.Info;
import me.qyh.web.Webs;

@Component("accessDeniedHandler")
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired
	private MessageSource messageSource;
	private RedirectStrategy rs = new DefaultRedirectStrategy();

	private static final String DEFAULT_PAGE = "login";
	private String errorPage = DEFAULT_PAGE;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		if (!response.isCommitted()) {
			if (Webs.isAjaxRequest(request)) {
				if (e instanceof CsrfException) {
					Webs.writeInfo(response, new Info(false,
							messageSource.getMessage("error.auth.csrf", new Object[] {}, request.getLocale())));
				} else {
					Webs.writeInfo(response, new Info(false,
							messageSource.getMessage("error.auth.denied", new Object[] {}, request.getLocale())));
				}
			} else {
				rs.sendRedirect(request, response, errorPage);
			}
		}
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

}
