package me.qyh.oauth2.security;

import me.qyh.oauth2.entity.OauthType;

public class OauthPrincipal {

	private String oauthUserId;
	private OauthType type;
	private Object token;

	public String getOauthUserId() {
		return oauthUserId;
	}

	public OauthType getType() {
		return type;
	}

	public OauthPrincipal(String oauthUserId, OauthType type) {
		this.oauthUserId = oauthUserId;
		this.type = type;
	}

	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "OauthPrincipal [oauthUserId=" + oauthUserId + ", type=" + type + "]";
	}
}
