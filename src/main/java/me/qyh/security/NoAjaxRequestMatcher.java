package me.qyh.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import me.qyh.web.Webs;

@Component(value = "noAjaxRequestMatcher")
public class NoAjaxRequestMatcher implements RequestMatcher {

	@Override
	public boolean matches(HttpServletRequest request) {
		return !Webs.isAjaxRequest(request);
	}

}
