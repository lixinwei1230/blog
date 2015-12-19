package me.qyh.oauth2.security;

import me.qyh.exception.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class Oauth2UserAuthencationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsChecker preAuthenticationChecks;
	@Autowired
	private UserDetailsChecker postAuthenticationChecks;
	@Autowired
	private OauthDetailsService oauthDetailsService;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Oauth2UserAuthencationToken token = (Oauth2UserAuthencationToken) authentication;
		if (!(token.getPrincipal() instanceof OauthPrincipal)) {
			throw new SystemException(token.getPrincipal() == null ? "认证过程中principal为null"
					: "认证过程中principal为" + token + "不是" + OauthPrincipal.class.getName());
		}
		OauthPrincipal principal = (OauthPrincipal) token.getPrincipal();
		UserDetails user = this.oauthDetailsService.loadUserByOauthPrincipal(principal);
		preAuthenticationChecks.check(user);
		postAuthenticationChecks.check(user);
		
		return new Oauth2UserAuthencationToken(user, authoritiesMapper.mapAuthorities(user.getAuthorities()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return Oauth2UserAuthencationToken.class.equals(authentication);
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}

}
