package me.qyh.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import me.qyh.exception.LogicException;

public class SafeUserNameChecker implements UserNameChecker {

	@Override
	public void check(String username) throws LogicException {
		if (!Jsoup.isValid(username, Whitelist.none())) {
			throw new LogicException("validation.username.invalid");
		}
	}

}
