package me.qyh.service.impl;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;

public interface ActivateSuccessHandler {

	void activateSuccess(User user) throws LogicException;

}
