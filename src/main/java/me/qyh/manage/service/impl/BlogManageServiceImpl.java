package me.qyh.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.SpaceDao;
import me.qyh.entity.blog.Blog;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.BlogManageService;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.BlogServiceImpl;

@Service
public class BlogManageServiceImpl extends BlogServiceImpl implements BlogManageService {

	@Autowired
	private SpaceDao spaceDao;
	@Autowired
	private TipServer tipServer;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteBlog(int id, TipMessage message) throws LogicException {
		Blog blog = load(id);

		deleteBlogFromDb(blog);

		message.setSender(UserContext.getUser());
		message.setReceiver(spaceDao.selectById(blog.getSpace().getId()).getUser());

		tipServer.sendTip(message);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void toggleBlogRecommand(int id, TipMessage message) throws LogicException {
		Blog blog = load(id);

		blog.setRecommend(!blog.isRecommend());
		blogDao.updateRecommend(blog);

		message.setSender(UserContext.getUser());
		message.setReceiver(spaceDao.selectById(blog.getSpace().getId()).getUser());

		tipServer.sendTip(message);
	}

	private Blog load(int id) throws LogicException {
		Blog blog = loadBlog(id);
		if (blog.getIsPrivate()) {
			throw new LogicException("error.blog.unvisible");
		}

		return blog;
	}

}
