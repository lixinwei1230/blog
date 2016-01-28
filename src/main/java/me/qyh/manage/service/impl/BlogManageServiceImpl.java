package me.qyh.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.SpaceDao;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.BlogManageService;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.BlogServiceImpl;
import me.qyh.utils.Validators;

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
		
		blogIndexHandler.rebuildBlogIndex(blog);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void pubScheduled() {
		BlogPageParam param = new BlogPageParam();
		param.setBegin(new Date(0));
		param.setEnd(new Date());
		param.setCurrentPage(1);
		param.setPageSize(Integer.MAX_VALUE);
		param.setStatus(BlogStatus.SCHEDULED);
		Page<Blog> blogs = blogIndexHandler.search(param);
		List<Blog> datas = blogs.getDatas();
		if(!Validators.isEmptyOrNull(datas)){
			for(Blog blog : datas){
				try {
					Blog db = loadBlog(blog.getId());
					if(db.getDel()){
						continue;
					}
					db.setStatus(BlogStatus.NORMAL);
					blogDao.update(db);
					blogIndexHandler.rebuildBlogIndex(db);
				} catch (LogicException e) {
					continue;
				}
			}
		}
	}

	private Blog load(int id) throws LogicException {
		Blog blog = loadBlog(id);
		if (blog.getIsPrivate()) {
			throw new LogicException("error.blog.unvisible");
		}

		return blog;
	}


}
