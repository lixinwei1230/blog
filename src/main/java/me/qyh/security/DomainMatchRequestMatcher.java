package me.qyh.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;

import me.qyh.web.tag.url.UrlHelper;

public class DomainMatchRequestMatcher implements RequestMatcher {

	private String url;
	@Autowired
	private UrlHelper urlHelper;

	@Override
	public boolean matches(HttpServletRequest request) {
		return matchDomain(request) && matchSpecifiedUrl(request);
	}

	private boolean matchSpecifiedUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}
		int queryParamIndex = uri.indexOf('?');
		if (queryParamIndex > 0) {
			uri = uri.substring(0, queryParamIndex);
		}
		if ("".equals(request.getContextPath())) {
			return uri.endsWith(url);
		}
		return uri.endsWith(request.getContextPath() + url);
	}

	private boolean matchDomain(HttpServletRequest request) {
		String domain = urlHelper.getDomain();
		return domain.equalsIgnoreCase(request.getServerName()) ? true
				: (domain.equalsIgnoreCase("www." + request.getServerName()))
						? true : false;
	}

	public DomainMatchRequestMatcher(String url) {
		this.url = url;
	}
}
