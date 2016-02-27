package me.qyh.oauth2.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.Oauth2Provider;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.entity.OauthUser.OauthType;
import me.qyh.oauth2.exception.Oauth2InvalidPrincipalException;
import me.qyh.oauth2.exception.Oauth2UnbindException;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;

public class Oauth2AuthencationFilter extends
		AbstractAuthenticationProcessingFilter {

	private static final String STATE = "state";
	private static final String SECRET_KEY = "secretKey";
	private static final String OAUTH_USER = "oauthUser";
	
	private Oauth2Provider provider;

	protected Oauth2AuthencationFilter(Oauth2Provider provider) {
		super(new Oauth2RequestMatcher(provider));
		this.provider = provider;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		Oauth2 oauth2 = provider.seekOauth2(request);
		OauthPrincipal principal = null;
		String code = request.getParameter("code");
		if (!Validators.isEmptyOrNull(code, true)) {
			checkState(request,oauth2.getType());
			principal = oauth2.getOauthPrincipal(code);
		} else {
			principal = getPrincipal(request.getSession());
			if (principal == null) {
				throw new Oauth2InvalidPrincipalException(oauth2.getType(),
						"无法获取code参数，并且不存在可用凭证");
			}
			removePrincipal(request.getSession());
			if (!principal.getType().equals(oauth2.getType())) {
				throw new Oauth2InvalidPrincipalException(oauth2.getType(),
						"凭证不匹配");
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

	/**
	 * 检查state值是否匹配，防止csrf
	 * 
	 * @param request
	 * @throws Oauth2InvalidStateException
	 */
	private void checkState(HttpServletRequest request, OauthType type) {
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
			throw new Oauth2InvalidPrincipalException(type,
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
		
		private Oauth2Provider provider;

		public Oauth2RequestMatcher(Oauth2Provider provider) {
			this.provider = provider;
		}

		public boolean matches(HttpServletRequest request) {
			return (provider.seekOauth2(request) != null);
		}
	}

}
