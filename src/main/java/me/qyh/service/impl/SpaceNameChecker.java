package me.qyh.service.impl;

import me.qyh.exception.LogicException;

public interface SpaceNameChecker {

	void check(String spaceName) throws LogicException;

}
