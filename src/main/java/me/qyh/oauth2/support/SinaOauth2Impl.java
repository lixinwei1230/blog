package me.qyh.oauth2.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import me.qyh.oauth2.AccessToken;
import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.entity.OauthAvatar;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.exception.Oauth2InvalidPrincipalException;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.utils.Validators;
import me.qyh.web.Webs;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

public class SinaOauth2Impl implements Oauth2, InitializingBean {

	private String appkey;
	private String appsecret;
	private String authorizationCodeUrl;
	private String authorizationUrl;
	private String redirectUri;
	private String userInfoUrl;
	private Set<String> scopes = new LinkedHashSet<String>();

	@Override
	public OauthUser queryUserInfo(OauthPrincipal principal) {
		UidToken token = (UidToken) principal.getToken();
		String url = String.format(userInfoUrl, token.getToken(), token.getUid());
		ObjectReader reader = Webs.reader();
		JsonNode node = null;
		try {
			JsonParser parser = reader.getFactory().createParser(new URL(url));
			node = reader.readTree(parser);
		} catch (IOException e) {
			throw new Oauth2Exception(OauthType.SINA, String.format("无法解析路径%s的内容:%s", url, e));
		}
		if (node.has("id")) {
			OauthUser user = new OauthUser();
			user.setType(OauthType.SINA);
			user.setUserid(token.getUid());
			user.setNickname(node.get("screen_name").asText());
			OauthAvatar avatar = new OauthAvatar(node.get("profile_image_url").asText());
			user.setAvatar(avatar);
			return user;
		} else {
			try {
				QueryUserInfoError error = reader.treeToValue(node, QueryUserInfoError.class);
				throw new Oauth2Exception(OauthType.SINA, String.format("获取token异常:%s", error));
			} catch (JsonProcessingException e) {
				throw new Oauth2Exception(OauthType.SINA, String.format("无法解析%s", node.toString()));
			}
		}

	}

	@Override
	public String getAuthorizeUrl(String state) {
		return new String(authorizationCodeUrl).concat("&state=").concat(state);
	}

	@Override
	public OauthPrincipal getOauthPrincipal(String code) {
		UidToken token = getToken(code);
		return new OauthPrincipal(token.getUid(), OauthType.SINA, token);
	}

	/**
	 * @param code
	 * @return
	 */
	private UidToken getToken(String code) {
		String url = authorizationUrl.concat("&code=").concat(code);
		String result = sendPost(url);
		JsonNode node = null;
		ObjectReader reader = Webs.reader();
		try {
			JsonParser parser = reader.getFactory().createParser(result);
			node = reader.readTree(parser);
		} catch (IOException e) {
			throw new Oauth2Exception(OauthType.SINA, String.format("无法解析%s", result), e);
		}
		if (node.has("access_token")) {
			String _token = node.get("access_token").asText();
			if (Validators.isEmptyOrNull(_token, true)) {
				throw new Oauth2InvalidPrincipalException(OauthType.SINA, "获取的token为空，可能取消了接入或者遭遇csrf攻击");
			}
			UidToken token = new UidToken();
			token.setExpireIn(node.get("expires_in").asLong());
			token.setToken(_token);
			token.setUid(node.get("uid").asText());
			return token;
		} else {
			try {
				Error error = reader.treeToValue(node, Error.class);
				throw new Oauth2Exception(OauthType.SINA, String.format("获取token异常:%s", error));
			} catch (JsonProcessingException e) {
				throw new Oauth2Exception(OauthType.SINA, String.format("无法解析%s", node.toString()));
			}
		}
	}
	
	@Override
	public OauthType getType() {
		return OauthType.SINA;
	}
	
	@Override
	public String callBackUrl() {
		return redirectUri;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		authorizationCodeUrl = String.format(authorizationCodeUrl, appkey, redirectUri, "code");
		if (!Validators.isEmptyOrNull(scopes)) {
			StringBuilder sb = new StringBuilder();
			for (String scope : scopes) {
				sb.append(scope).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			authorizationCodeUrl += ("&scope" + sb.toString());
		}
		authorizationUrl = String.format(authorizationUrl, appkey, appsecret, "authorization_code", redirectUri);
	}

	private static String sendPost(String url) {
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			
		} finally {
			IOUtils.closeQuietly(in);
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	private class UidToken extends AccessToken {
		private String uid;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Error {
		@JsonProperty("error")
		private String error;
		@JsonProperty("error_code")
		private String code;
		@JsonProperty("error_description")
		private String description;

		@Override
		public String toString() {
			return "Error [error=" + error + ", code=" + code + ", description=" + description + "]";
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private class QueryUserInfoError {
		@JsonProperty("error")
		private String error;
		@JsonProperty("error_code")
		private String code;
		@JsonProperty("request")
		private String request;

		@Override
		public String toString() {
			return "QueryUserInfoError [error=" + error + ", code=" + code + ", request=" + request + "]";
		}
	}

	public void setAuthorizationCodeUrl(String authorizationCodeUrl) {
		this.authorizationCodeUrl = authorizationCodeUrl;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setScopes(Set<String> scopes) {
		this.scopes = scopes;
	}

	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

}
