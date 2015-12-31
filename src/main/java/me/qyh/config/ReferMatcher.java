package me.qyh.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import me.qyh.utils.Validators;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

@Component("fileWriteMatcher")
public class ReferMatcher implements RequestMatcher {

	@Autowired
	private UrlHelper urlHelper;

	private String[] allowDomains;
	private boolean enableLocal = true;
	private boolean enableNull = true;

	private static final String LOCALHOST = "localhost";
	private static final String LOCALIP = "127.0.0.1";
	private final AntPathMatcher matcher = new AntPathMatcher();

	@Override
	public boolean matches(HttpServletRequest request) {
		String referer = Webs.getReferer(request);
		if (referer == null) {
			return enableNull;
		}
		if (referer.trim().equals("")) {
			return false;
		}
		try {
			UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(referer);
			referer = uri.build().getHost();
			if (isLocal(referer)) {
				return enableLocal;
			}
			if (!Validators.validateIp(referer) && !(urlHelper.getRootDomain() == null)) {
				return matchDomain(referer);
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private boolean isLocal(String referer) {
		return LOCALHOST.equalsIgnoreCase(referer) || LOCALIP.equals(referer);
	}

	private boolean matchDomain(String referer) {
		String rootDomain = urlHelper.getRootDomain();
		if (rootDomain.equals(referer) || matcher.match("*." + rootDomain, referer)) {
			return true;
		}
		if (!Validators.isEmptyOrNull(allowDomains)) {
			for (String domain : allowDomains) {
				if (matcher.match("*." + rootDomain, domain)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setAllowDomains(String[] allowDomains) {
		this.allowDomains = allowDomains;
	}

	public void setEnableLocal(boolean enableLocal) {
		this.enableLocal = enableLocal;
	}

	public void setEnableNull(boolean enableNull) {
		this.enableNull = enableNull;
	}
}
