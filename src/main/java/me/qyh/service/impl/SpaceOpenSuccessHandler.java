package me.qyh.service.impl;

import me.qyh.entity.Space;
import me.qyh.exception.LogicException;

public interface SpaceOpenSuccessHandler {

	void handler(Space opened) throws LogicException;

}
