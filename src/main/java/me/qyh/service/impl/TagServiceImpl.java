package me.qyh.service.impl;

import java.util.List;

import me.qyh.dao.UserTagDao;
import me.qyh.entity.tag.UserTag;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserTagPageParam;
import me.qyh.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("tagService")
public class TagServiceImpl implements TagService {

	@Autowired
	private UserTagDao userTagDao;

	@Override
	@Transactional(readOnly = true)
	public Page<UserTag> findUserTags(UserTagPageParam param) {
		int count = userTagDao.selectCount(param);
		List<UserTag> tags = userTagDao.selectPage(param);
		return new Page<UserTag>(param, count, tags);
	}

}
