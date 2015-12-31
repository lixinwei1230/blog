package me.qyh.web.tag.url;

import org.springframework.beans.factory.InitializingBean;

import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

public class UrlHelper implements InitializingBean {

	private String domainAndPort;
	private boolean enableSpaceDomain;
	private String contextPath;
	private String domain;
	private String protocol;
	private String rootDomain;

	public UrlHelper(String domainAndPort, boolean enableSpaceDomain, String contextPath, String protocol) {
		this.domainAndPort = domainAndPort;
		this.enableSpaceDomain = enableSpaceDomain;
		this.contextPath = contextPath;
		this.protocol = protocol;

		String domain = "";
		if (domainAndPort.indexOf(":") != -1) {
			domain = domainAndPort.split(":")[0];
		} else {
			domain = domainAndPort;
		}
		this.domain = domain;
		if (domain.indexOf(".") == -1) {
			this.rootDomain = null;
		} else {
			String[] chunks = domain.split("\\.");
			int length = chunks.length;
			this.rootDomain = chunks[length - 2] + "." + chunks[length - 1];
		}
	}

	public String getUrl() {
		return getUrl(protocol);
	}

	public String getUrl(String protocol) {
		return protocol + "://" + domainAndPort + contextPath;
	}

	public String getUrlByUser(User user, boolean myMenu) {
		return getUrlByUser(user, myMenu, protocol);
	}

	public String getUrlByUser(User user, boolean myMenu, String protocol) {
		Space space = user.getSpace();
		if (myMenu) {
			if (space != null && enableSpaceDomain) {
				return getUrlBySpace(space, protocol) + "/my";
			} else {
				return protocol + "://" + domainAndPort + contextPath + "/my";
			}
		} else {
			if (space != null) {
				return getUrlBySpace(space, protocol);
			} else {
				return protocol + "://" + domainAndPort + contextPath + "/user/" + user.getId();
			}
		}
	}

	public String getUrlBySpace(Space space) {
		return getUrlBySpace(space, protocol);
	}

	public String getUrlBySpace(Space space, String protocol) {
		String url = "";
		if (enableSpaceDomain) {
			if (domainAndPort.startsWith("www.")) {
				url = space.getId() + domainAndPort.substring(domainAndPort.indexOf("."));
			} else {
				url = space.getId() + "." + domainAndPort;
			}
			return protocol + "://" + url + contextPath;
		}
		return protocol + "://" + domainAndPort + contextPath + "/space/" + space.getId();
	}

	public String getDomainAndPort() {
		return domainAndPort;
	}

	public boolean isEnableSpaceDomain() {
		return enableSpaceDomain;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getDomain() {
		return domain;
	}

	public String getProtocol() {
		return protocol;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Validators.isEmptyOrNull(domainAndPort, true)) {
			throw new SystemException("domainAndPort不能为空");
		}
		if (Validators.isEmptyOrNull(protocol, true)) {
			throw new SystemException("protocol不能为空");
		}
		if (contextPath == null) {
			throw new SystemException("contextPath不能为null");
		}
	}

	public String getRootDomain() {
		return rootDomain;
	}
}
