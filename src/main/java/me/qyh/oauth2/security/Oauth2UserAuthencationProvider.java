package me.qyh.oauth2.security;

import me.qyh.exception.SystemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;

public class Oauth2UserAuthencationProvider implements AuthenticationProvider {

	@Autowired
	private OauthDetailsService oauthDetailsService;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	private final AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Oauth2UserAuthencationToken token = (Oauth2UserAuthencationToken) authentication;
		if (!(token.getPrincipal() instanceof OauthPrincipal)) {
			throw new SystemException(token.getPrincipal() == null ? "认证过程中principal为null"
					: "认证过程中principal为" + token + "不是" + OauthPrincipal.class.getName());
		}
		OauthPrincipal principal = (OauthPrincipal) token.getPrincipal();
		UserDetails user = this.oauthDetailsService.loadUserByOauthPrincipal(principal);
		checker.check(user);
		
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
