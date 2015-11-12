package me.qyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.qyh.dao.PageDao;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.page.Page;
import me.qyh.page.PageType;

@Component
public class DefaultActivateSuccessHandler implements ActivateSuccessHandler {

	@Autowired
	private PageDao pageDao;

	@Override
	public void activateSuccess(User user) throws LogicException {
		pageDao.insert(new Page(user, PageType.HOMEPAGE));
	}

}
