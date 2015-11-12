package me.qyh.security;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component("postAuthenticationChecks")
public class UserPostAuthenticationCheckers implements UserDetailsChecker {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();

	public void check(UserDetails user) {
		if (!user.isCredentialsNonExpired()) {

			throw new CredentialsExpiredException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.credentialsExpired",
					"User credentials have expired"));
		}
	}
}