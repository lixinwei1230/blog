package me.qyh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.UserTagCountDao;
import me.qyh.dao.UserTagDao;
import me.qyh.dao.WebTagCountDao;
import me.qyh.dao.WebTagDao;
import me.qyh.entity.tag.UserTag;
import me.qyh.entity.tag.UserTagCount;
import me.qyh.entity.tag.WebTag;
import me.qyh.entity.tag.WebTagCount;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserTagPageParam;
import me.qyh.pageparam.WebTagPageParam;
import me.qyh.service.TagService;

@Service("tagService")
public class TagServiceImpl implements TagService {

	@Autowired
	private WebTagDao webTagDao;
	@Autowired
	private WebTagCountDao webTagCountDao;
	@Autowired
	private UserTagDao userTagDao;
	@Autowired
	private UserTagCountDao userTagCountDao;

	@Override
	@Transactional(readOnly = true)
	public Page<WebTag> findWebTags(WebTagPageParam param) {
		int count = webTagDao.selectCount(param);
		List<WebTag> tags = webTagDao.selectPage(param);
		for (WebTag tag : tags) {
			List<WebTagCount> counts = webTagCountDao.selectByTag(tag);
			tag.addCounts(counts.toArray(new WebTagCount[counts.size()]));
		}
		return new Page<WebTag>(param, count, tags);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserTag> findUserTags(UserTagPageParam param) {
		int count = userTagDao.selectCount(param);
		List<UserTag> tags = userTagDao.selectPage(param);
		for (UserTag tag : tags) {
			List<UserTagCount> counts = userTagCountDao.selectByTag(tag);
			tag.addCounts(counts.toArray(new UserTagCount[counts.size()]));
		}
		return new Page<UserTag>(param, count, tags);
	}

}
