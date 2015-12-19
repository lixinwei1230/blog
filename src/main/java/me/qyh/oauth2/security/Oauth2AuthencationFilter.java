package me.qyh.oauth2.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.exception.Oauth2InvalidAccessTokenException;
import me.qyh.oauth2.exception.Oauth2UnbindException;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class Oauth2AuthencationFilter extends
		AbstractAuthenticationProcessingFilter {

	private static final String STATE = "state";
	private static final String SECRET_KEY = "secretKey";
	private static final String OAUTH_USER = "oauthUser";

	private List<Oauth2> oauth2s;
	private static final Map<String,Oauth2> paths = new HashMap<String,Oauth2>();

	protected Oauth2AuthencationFilter(List<Oauth2> oauth2s) {
		super(new Oauth2RequestMatcher());
		this.oauth2s = oauth2s;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		Oauth2 oauth2 = getOauth2(request);
		OauthPrincipal principal = null;
		String code = request.getParameter("code");
		if (!Validators.isEmptyOrNull(code, true)) {
			checkState(request);
			principal = oauth2.getOauthPrincipal(code);
		} else {
			principal = getPrincipal(request.getSession());
			if (principal == null) {
				throw new InvalidPrincipalException("无法获取code参数，并且不存在可用凭证");
			}
			removePrincipal(request.getSession());
			if (!principal.getType().equals(oauth2.getType())) {
				throw new InvalidPrincipalException("凭证不匹配");
			}
		}
		Oauth2UserAuthencationToken token = new Oauth2UserAuthencationToken(
				principal);
		token.setDetails(authenticationDetailsSource.buildDetails(request));
		try {
			return this.getAuthenticationManager().authenticate(token);
		} catch (Oauth2UnbindException e) {
			OauthPrincipal _principal = e.getPrincipal();
			putInfo(request.getSession(), oauth2.queryUserInfo(_principal),
					_principal);
			throw e;
		} catch (Oauth2InvalidAccessTokenException e) {
			throw new InvalidTokenException(e.getMessage());
		}
	}

	private void removePrincipal(HttpSession session) {
		String key = (String) session.getAttribute(SECRET_KEY);
		if (key != null) {
			session.removeAttribute(key);
			session.removeAttribute((String) session.getAttribute(key));
		}
	}

	private OauthPrincipal getPrincipal(HttpSession session) {
		String key = (String) session.getAttribute(SECRET_KEY);
		if (key != null) {
			return (OauthPrincipal) session.getAttribute(key);
		}
		return null;
	}

	private Oauth2 getOauth2(HttpServletRequest request) {
		for(Map.Entry<String, Oauth2> map : paths.entrySet()){
			if(Oauth2RequestMatcher.match(request, map.getKey())){
				return map.getValue();
			}
		}
		return null;
	}

	/**
	 * 检查state值是否匹配，防止csrf
	 * 
	 * @param request
	 * @throws Oauth2InvalidStateException
	 */
	private void checkState(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean valid = false;
		if (session != null) {
			String state = (String) session.getAttribute(STATE);
			session.removeAttribute(STATE);
			String _state = request.getParameter(STATE);
			valid = !(Validators.isEmptyOrNull(_state, true) || !_state
					.equals(state));
		}
		if (!valid) {
			throw new InvalidStateException(
					"oauth2 server传来的state无法被找到或者匹配失败，无法完成认证");
		}
	}

	private void putInfo(HttpSession session, OauthUser user,
			OauthPrincipal principal) {
		String securityKey = Strings.uuid();
		session.setAttribute(SECRET_KEY, securityKey);
		session.setAttribute(securityKey, principal);
		session.setAttribute(OAUTH_USER, user);
	}

	private static final class Oauth2RequestMatcher implements RequestMatcher {

		public boolean matches(HttpServletRequest request) {
			for (String path : paths.keySet()) {
				if (match(request, path)) {
					return true;
				}
			}
			return false;
		}

		private static boolean match(HttpServletRequest req, String path) {
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
	}

	private class InvalidStateException extends AuthenticationException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidStateException(String msg) {
			super(msg);
		}
	}

	private class InvalidTokenException extends AuthenticationException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidTokenException(String msg) {
			super(msg);
		}
	}

	private class InvalidPrincipalException extends AuthenticationException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidPrincipalException(String msg) {
			super(msg);
		}
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		for (Oauth2 oauth2 : oauth2s) {
			String callBackUrl = oauth2.callBackUrl();
			UriComponents uc = UriComponentsBuilder.fromHttpUrl(callBackUrl)
					.build();
			paths.put(uc.getPath(),oauth2);
		}
	}
	
}
