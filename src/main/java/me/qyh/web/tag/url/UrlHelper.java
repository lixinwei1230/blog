package me.qyh.web.tag.url;

import org.springframework.beans.factory.InitializingBean;

import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

public class UrlHelper implements InitializingBean{

	private String domainAndPort;
	private boolean enableSpaceDomain;
	private String contextPath;
	private String domain;
	private String protocal;

	public UrlHelper(String domainAndPort, boolean enableSpaceDomain, String contextPath , String protocal) {
		super();
		this.domainAndPort = domainAndPort;
		this.enableSpaceDomain = enableSpaceDomain;
		this.contextPath = contextPath;
		this.protocal = protocal;

		String domain = "";
		if (domainAndPort.indexOf(":") != -1) {
			domain = domainAndPort.split(":")[0];
		} else {
			domain = domainAndPort;
		}
		this.domain = domain;
	}

	public String getUrl() {
		return getUrl(protocal);
	}
	
	public String getUrl(String protocal){
		return protocal + "://" + domainAndPort + contextPath;
	}
	
	public String getUrlByUser(User user, boolean myMenu) {
		return getUrlByUser(user, myMenu, protocal);
	}

	public String getUrlByUser(User user, boolean myMenu , String protocal) {
		Space space = user.getSpace();
		if (myMenu) {
			if (space != null && enableSpaceDomain) {
				return getUrlBySpace(space,protocal) + "/my";
			} else {
				return protocal + "://" + domainAndPort + contextPath + "/my";
			}
		} else {
			if (space != null) {
				return getUrlBySpace(space,protocal);
			} else {
				return protocal + "://" + domainAndPort + contextPath + "/user/" + user.getId();
			}
		}
	}
	
	public String getUrlBySpace(Space space) {
		return getUrlBySpace(space, protocal);
	}

	public String getUrlBySpace(Space space,String protocal) {
		String url = "";
		if (enableSpaceDomain) {
			if (domainAndPort.startsWith("www.")) {
				url = space.getId() + domainAndPort.substring(domainAndPort.indexOf("."));
			} else {
				url = space.getId() + "." + domainAndPort;
			}
			return protocal + "://" + url + contextPath;
		}
		return protocal + "://" + domainAndPort + contextPath + "/space/" + space.getId();
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

	public String getProtocal() {
		return protocal;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(Validators.isEmptyOrNull(domainAndPort,true)){
			throw new SystemException("domainAndPort不能为空");
		}
		if(Validators.isEmptyOrNull(protocal,true)){
			throw new SystemException("protocal不能为空");
		}
		if(contextPath == null){
			throw new SystemException("contextPath不能为null");
		}
	}

}
