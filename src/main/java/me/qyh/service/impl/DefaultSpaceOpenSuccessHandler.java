package me.qyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.qyh.dao.PageDao;
import me.qyh.entity.Space;
import me.qyh.exception.LogicException;
import me.qyh.page.Page;
import me.qyh.page.PageType;

@Component
public class DefaultSpaceOpenSuccessHandler implements SpaceOpenSuccessHandler {

	@Autowired
	private PageDao pageDao;

	@Override
	public void handler(Space space) throws LogicException {
		pageDao.insert(new Page(space.getUser(), PageType.BLOG));
	}

}
