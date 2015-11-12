package me.qyh.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import me.qyh.entity.Space;
import me.qyh.entity.User;

public final class UserContext {

	public static User getUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication auth = context.getAuthentication();
			if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
				return (User) auth.getPrincipal();
			}
		}
		return null;
	}

	public static Space getSpace() {
		User user = getUser();
		if (user != null) {
			return user.getSpace();
		}
		return null;
	}

}
