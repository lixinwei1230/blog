package me.qyh.oauth2.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.exception.Oauth2UnbindException;
import me.qyh.security.DefaultLoginFailureHandler;
import me.qyh.web.tag.url.UrlHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

public class Oauth2AuthenticationFailureHandler extends DefaultLoginFailureHandler{
	
	private String bindUrl;
	
	public Oauth2AuthenticationFailureHandler(String bindUrl) {
		this.bindUrl = bindUrl;
 	}

	private static final  Logger logger = LoggerFactory.getLogger(Oauth2AuthencationFilter.class);
	
	@Autowired
	private UrlHelper urlHelper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest req,
			HttpServletResponse res, AuthenticationException exp)
			throws IOException, ServletException {
		if(exp instanceof Oauth2UnbindException){
			res.sendRedirect(urlHelper.getUrl() + bindUrl);
			return;
		}
		if(exp instanceof Oauth2Exception){
			logger.error(exp.getMessage(), exp);
			res.sendError(500);
			return ;
		}
		res.sendRedirect(urlHelper.getUrl() + defaultFailureUrl);
	}

}
