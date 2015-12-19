package me.qyh.oauth2.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.exception.Oauth2UnbindException;
import me.qyh.web.tag.url.UrlHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

public class Oauth2ExceptionTranslationFilter extends GenericFilterBean {

	@Autowired
	private UrlHelper urlHelper;
	
	private static final  Logger logger = LoggerFactory.getLogger(Oauth2AuthencationFilter.class);

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			chain.doFilter(request, response);
		} catch (Oauth2Exception ex) {
			logger.error(ex.getMessage(), ex);
			response.sendError(500);
		} catch (Oauth2UnbindException e) {
			response.sendRedirect(urlHelper.getUrl() + "/oauth/bind/index");
			return;
		} 
	}

}
