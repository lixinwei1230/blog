package me.qyh.oauth2.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface OauthDetailsService {

	UserDetails loadUserByOauthPrincipal(OauthPrincipal oauth2Principal)
			throws UsernameNotFoundException;

}
