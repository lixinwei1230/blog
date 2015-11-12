package me.qyh.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import me.qyh.exception.LogicException;

public class SafeSpaceNameChecker implements SpaceNameChecker {

	@Override
	public void check(String spaceName) throws LogicException {
		if (!Jsoup.isValid(spaceName, Whitelist.none())) {
			throw new LogicException("validation.space.id.invalid");
		}
	}

}
