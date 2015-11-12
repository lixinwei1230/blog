package me.qyh.web.tag.url;

import me.qyh.entity.Space;
import me.qyh.entity.User;

public class UglyUrlHelper implements UrlHelper {

	private String domainAndPort;
	private boolean enableSpaceDomain;
	private String contextPath;
	private String domain;

	public UglyUrlHelper(String domainAndPort, boolean enableSpaceDomain,
			String contextPath) {
		super();
		this.domainAndPort = domainAndPort;
		this.enableSpaceDomain = enableSpaceDomain;
		this.contextPath = contextPath;

		String domain = "";
		if (domainAndPort.indexOf(":") != -1) {
			domain = domainAndPort.split(":")[0];
		} else {
			domain = domainAndPort;
		}
		this.domain = domain;
	}

	public String getUrl() {
		return domainAndPort + contextPath;
	}

	public String getUrlByUser(User user, boolean myMenu) {
		Space space = user.getSpace();
		if (myMenu) {
			if (space != null && enableSpaceDomain) {
				return getUrlBySpace(space) + "/my";
			} else {
				return domainAndPort + contextPath + "/my";
			}
		} else {
			if (space != null) {
				return getUrlBySpace(space);
			} else {
				return domainAndPort + contextPath + "/user/" + user.getId();
			}
		}
	}

	public String getUrlBySpace(Space space) {
		String url = "";
		if (enableSpaceDomain) {
			if (domainAndPort.startsWith("www.")) {
				url = space.getId()
						+ domainAndPort.substring(domainAndPort.indexOf("."));
			} else {
				url = space.getId() + "." + domainAndPort;
			}
			return url + contextPath;
		}
		return domainAndPort + contextPath + "/space/" + space.getId();
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

}
