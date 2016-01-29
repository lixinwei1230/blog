package me.qyh.security;

import me.qyh.web.tag.url.UrlHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public abstract class DefaultLoginFailureHandler implements AuthenticationFailureHandler{
	
	private static final String LOGIN = "/login";
	protected String defaultFailureUrl = LOGIN;
	@Autowired
	protected UrlHelper urlHelper;
	
	protected DefaultLoginFailureHandler() {
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
