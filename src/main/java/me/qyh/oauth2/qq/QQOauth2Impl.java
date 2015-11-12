package me.qyh.oauth2.qq;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectReader;

import me.qyh.oauth2.Oauths;
import me.qyh.oauth2.entity.OauthAvatar;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.exception.Oauth2ConnectionException;
import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.oauth2.exception.Oauth2InvalidAccessTokenException;
import me.qyh.oauth2.security.OauthPrincipal;
import me.qyh.utils.Validators;

public class QQOauth2Impl implements QQOauth2, InitializingBean {

	private static final String RESPONSE_TYPE = "code";
	private static final String GRANT_TYPE = "authorization_code";

	@Autowired
	private ObjectReader reader;
	private String openIdUrl;
	private String appId;
	private String appKey;
	private String authorizationCodeUrl;
	private String authorizationUrl;
	private String redirectUri;
	private String userInfoUrl;
	private Set<String> scopes = new LinkedHashSet<String>();

	/**
	 * 客户端状态值
	 * 
	 * @param state
	 * @return
	 */
	@Override
	public String getAuthorizeUrl(String state, HttpServletRequest request) {
		return new String(authorizationCodeUrl).concat("&state=").concat(state);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("response_type", RESPONSE_TYPE);
		params.add("client_id", appId);
		params.add("redirect_uri", redirectUri);
		if (!Validators.isEmptyOrNull(scopes)) {
			StringBuilder sb = new StringBuilder();
			for (String scope : scopes) {
				sb.append(scope).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			params.add("scope", sb.toString());
		}
		authorizationCodeUrl = UriComponentsBuilder.fromHttpUrl(authorizationCodeUrl).queryParams(params).build(true)
				.toUriString();

		params.clear();
		params.add("grant_type", GRANT_TYPE);
		params.add("client_id", appId);
		params.add("client_secret", appKey);
		params.add("redirect_uri", redirectUri);
		authorizationUrl = UriComponentsBuilder.fromHttpUrl(authorizationUrl).queryParams(params).build(true)
				.toUriString();

		params.clear();
		params.add("oauth_consumer_key", appId);
		params.add("format", "json");
		userInfoUrl = UriComponentsBuilder.fromHttpUrl(userInfoUrl).queryParams(params).build(true).toString();

	}

	@Override
	public OpenId getOpenId(AccessToken token) {
		String url = new String(openIdUrl).concat("?access_token=").concat(token.getToken());
		QQHttpResult result = sendHttpsGet(url);
		if (result.hasError) {
			throw new Oauth2Exception(OauthType.QQ,
					String.format("获取openId时发生了一个错误，错误码为%s", result.error.getErrorCode()));
		}
		return result.parseJsonObj(OpenId.class);
	}

	@Override
	public AccessToken getAccessToken(String code) {
		String url = new String(authorizationUrl).concat("&code=").concat(code);
		QQHttpResult result = sendHttpsGet(url);
		if (result.hasError) {
			throw new Oauth2Exception(OauthType.QQ,
					String.format("获取AccessToken时发生了一个错误，错误码为%s", result.error.getErrorCode()));
		}

		AccessToken token = new AccessToken();

		token.setToken(result.getResult("access_token"));
		if (Validators.isEmptyOrNull(token.getToken(), true)) {
			throw new Oauth2InvalidAccessTokenException(OauthType.QQ, "获取的token为空，可能取消了接入或者遭遇csrf攻击");
		}
		token.setExpireIn(Long.parseLong(result.getResult("expires_in")));
		token.setRefreshToken(result.getResult("refresh_token"));
		return token;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setAuthorizationCodeUrl(String authorizationCodeUrl) {
		this.authorizationCodeUrl = authorizationCodeUrl;
	}

	public void setAuthorizationUrl(String authorizationUrl) {
		this.authorizationUrl = authorizationUrl;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public void setScopes(Set<String> scopes) {
		this.scopes = scopes;
	}

	public void setOpenIdUrl(String openIdUrl) {
		this.openIdUrl = openIdUrl;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

	private QQHttpResult sendHttpsGet(String urlAndParams) {
		return new QQHttpResult(_sendHttpsGet(urlAndParams));
	}

	private String _sendHttpsGet(String urlAndParams) {
		try {
			String response = Oauths.sendHttpsGet(urlAndParams);
			if (Validators.isEmptyOrNull(response, true)) {
				throw new Oauth2ConnectionException(OauthType.QQ,
						String.format("链接%s返回的相应信息为空，可能是链接有问题", urlAndParams));
			}
			return response;
		} catch (IOException e) {
			throw new Oauth2ConnectionException(OauthType.QQ, e.getMessage(), e);
		}
	}

	private class QQHttpResult {

		/**
		 * json格式前缀
		 */
		private static final String JSON_PREFIX = "callback";

		/**
		 * json片段，有callback截取获得
		 */
		private String json;

		private Map<String, String> results = new HashMap<String, String>();

		private QQOauth2Error error = null;

		private boolean hasError;

		public QQHttpResult(String response) {
			if (response.startsWith(JSON_PREFIX)) {
				json = response.substring(response.indexOf('{'), response.lastIndexOf('}') + 1);
				hasError = json.contains("error");
				error = parseJsonObj(QQOauth2Error.class);
			} else if (response.indexOf("&") != -1) {
				String[] params = response.split("&");
				for (String param : params) {
					String[] splited = param.split("=");
					results.put(splited[0], splited[1]);
				}
				// 接口调用有错误时，会返回code和msg字段，以url参数对的形式返回，value部分会进行url编码（UTF-8）。
				if (results.containsKey("code")) {
					hasError = true;
					error = new QQOauth2Error();
					error.setErrorCode(results.get("code"));
				}
			} else {
				throw new Oauth2Exception(OauthType.QQ, String.format("无法解析%s", response));
			}
		}

		/**
		 * 将json信息转化为JsonObj
		 * 
		 * @param t
		 * @param reader
		 * @return
		 * @throws Oauth2Exception
		 *             如果转化出现异常
		 */
		private <T> T parseJsonObj(Class<T> t) {
			JsonParser parser;
			try {
				parser = reader.getFactory().createParser(json);
				return reader.readValue(parser, t);
			} catch (IOException e) {
				throw new Oauth2Exception(OauthType.QQ, e.getMessage(), e);
			}
		}

		private String getResult(String key) {
			return results.get(key);
		}
	}

	@Override
	public OauthUser queryUserInfo(OauthPrincipal principal) {
		String url = new String(userInfoUrl).concat("&access_token=")
				.concat(((AccessToken) principal.getToken()).getToken()).concat("&openid=")
				.concat(principal.getOauthUserId());
		String response = _sendHttpsGet(url);
		try {
			JsonParser parser = reader.getFactory().createParser(response);
			QQUser quser = reader.readValue(parser, QQUser.class);
			if (!"0".equals(quser.getRet())) {
				throw new Oauth2Exception(OauthType.QQ, String.format("获取用户信息时发生了一个错误，错误码为%s", quser.getRet()));
			}
			OauthUser user = new OauthUser();
			if (quser.getAvatar() != null) {
				user.setAvatar(new OauthAvatar(quser.getAvatar()));
			}
			user.setUserid(principal.getOauthUserId());
			user.setType(OauthType.QQ);
			user.setNickname(quser.getNickname());
			return user;
		} catch (IOException e) {
			throw new Oauth2Exception(OauthType.QQ, url + ":" + e.getMessage(), e);
		}
	}

}
