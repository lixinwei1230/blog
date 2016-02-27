package me.qyh.oauth2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import me.qyh.oauth2.entity.OauthUser.OauthType;

public class Oauth2Provider implements InitializingBean {

	private List<Oauth2> oauth2s;
	private Map<String, Oauth2> urlMaps = new HashMap<String, Oauth2>();

	public Oauth2 seekOauth2(HttpServletRequest req) {
		for (Map.Entry<String, Oauth2> map : urlMaps.entrySet()) {
			if (match(req, map.getKey())) {
				return map.getValue();
			}
		}
		return null;
	}
	
	public Oauth2 seekOauth2(OauthType type){
		for(Oauth2 oauth2 : oauth2s){
			if(oauth2.getType().equals(type)){
				return oauth2;
			}
		}
		return null;
	}

	private boolean match(HttpServletRequest req, String path) {
		String uri = req.getRequestURI();
		int pathParamIndex = uri.indexOf(';');

		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}
		if ("".equals(req.getContextPath())) {
			return uri.endsWith(path);
		}
		return uri.endsWith(req.getContextPath() + path);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Oauth2 oauth2 : oauth2s) {
			String callBackUrl = oauth2.callBackUrl();
			UriComponents uc = UriComponentsBuilder.fromHttpUrl(callBackUrl)
					.build();
			urlMaps.put(uc.getPath(), oauth2);
		}
	}

	public void setOauth2s(List<Oauth2> oauth2s) {
		this.oauth2s = oauth2s;
	}
}
