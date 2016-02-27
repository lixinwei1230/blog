package me.qyh.oauth2.security;

import me.qyh.oauth2.AccessToken;
import me.qyh.oauth2.entity.OauthUser.OauthType;

public class OauthPrincipal {

	private String oauthUserId;
	private OauthType type;
	private AccessToken token;

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
	
	public OauthPrincipal(String oauthUserId, OauthType type, AccessToken token) {
		this.oauthUserId = oauthUserId;
		this.type = type;
		this.token = token;
	}

	public AccessToken getToken() {
		return token;
	}

	public void setToken(AccessToken token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "OauthPrincipal [oauthUserId=" + oauthUserId + ", type=" + type + "]";
	}
}
