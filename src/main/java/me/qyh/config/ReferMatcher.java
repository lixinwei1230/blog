package me.qyh.config;

import javax.servlet.http.HttpServletRequest;

import me.qyh.utils.Validators;
import me.qyh.web.tag.url.UrlHelper;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UriComponentsBuilder;

@Component("fileWriteMatcher")
public class ReferMatcher implements RequestMatcher, InitializingBean {

	@Autowired
	private UrlHelper urlHelper;
	private String rootDomain;

	private String[] allowDomains;
	private boolean enableLocal = true;
	private boolean enableNull;

	private static final String LOCALHOST = "localhost";
	private static final String LOCALIP = "127.0.0.1";
	private static final String REFERER = "Referer";

	@Override
	public boolean matches(HttpServletRequest request) {
		String referer = request.getHeader(REFERER);
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
			if (!Validators.validateIp(referer) && !(rootDomain == null)) {
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
		AntPathMatcher matcher = new AntPathMatcher();
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

	@Override
	public void afterPropertiesSet() throws Exception {
		String domain = urlHelper.getDomain();
		if (domain.indexOf(".") == -1) {
			this.rootDomain = null;
		} else {
			String[] chunks = domain.split("\\.");
			int length = chunks.length;
			if (length < 2) {
				this.rootDomain = null;
			} else {
				this.rootDomain = chunks[length - 2] + "." + chunks[length - 1];
			}
		}
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
