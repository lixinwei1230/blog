package me.qyh.service;

import me.qyh.entity.Space;
import me.qyh.exception.LogicException;

public interface SpaceService {

	void openSpace(Space space) throws LogicException;

}
