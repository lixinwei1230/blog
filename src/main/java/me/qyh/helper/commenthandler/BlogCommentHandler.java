package me.qyh.helper.commenthandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import me.qyh.dao.BlogDao;
import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.Scope;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.security.UserContext;
import me.qyh.server.SpaceServer;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.server.UserServer;
import me.qyh.service.CommentHandler;

public class BlogCommentHandler implements CommentHandler {

	@Autowired
	private BlogDao blogDao;
	@Autowired
	private WebFreemarkers freeMarkers;
	@Autowired
	private SpaceServer spaceServer;
	@Autowired
	private UserServer userServer;
	@Autowired
	private MessageSource messageSource;

	@Override
	public User getScopeUser(CommentScope target) throws LogicException {
		Integer blogId = getBlogId(target);
		Blog blog = blogDao.selectById(blogId);
		if (blog == null) {
			throw new LogicException("error.blog.notexists");
		}
		Space space = spaceServer.getSpaceById(blog.getSpace().getId());
		return space.getUser();
	}

	private Integer getBlogId(CommentScope target) {
		try {
			return Integer.parseInt(target.getScopeId());
		} catch (NumberFormatException e) {
			throw new SystemException(
					this.getClass().getName() + "-getBlogId:" + e.getMessage(),
					e);
		}
	}

	@Override
	public boolean match(CommentScope target) {
		return Blog.class.getSimpleName().equals(target.getScope());
	}

	@Override
	public void doAuthencationBeforeInsert(Comment comment)
			throws CommentAuthencationException, LogicException {
		Integer blogId = getBlogId(comment.getScope());
		Blog blog = blogDao.selectById(blogId);
		if (blog == null || BlogStatus.RECYCLER.equals(blog.getStatus())) {
			throw new LogicException("error.blog.notexists");
		}
		if (Scope.PRIVATE.equals(blog.getScope())
				&& !blog.getSpace().equals(UserContext.getSpace())) {
			throw new CommentAuthencationException(
					"error.blogCommentHandler.noAuthencation");
		}
		if (Scope.PRIVATE.equals(blog.getCommentScope())
				&& !blog.getSpace().equals(UserContext.getSpace())) {
			throw new CommentAuthencationException(
					"error.blogCommentHandler.notAllowComment");
		}
	}

	@Override
	public void doAuthencationBeforeQuery(CommentScope target)
			throws CommentAuthencationException, LogicException {
		Integer blogId = getBlogId(target);
		Blog blog = blogDao.selectById(blogId);
		if (blog == null || blog.getStatus().equals(BlogStatus.RECYCLER)) {
			throw new LogicException("error.blog.notexists");
		}
		if (Scope.PRIVATE.equals(blog.getScope())
				&& !blog.getSpace().equals(UserContext.getSpace())) {
			throw new CommentAuthencationException(
					"error.blogCommentHandler.noAuthencation");
		}
	}

	@Override
	public void tip(User scopeUser, Comment comment, TipServer tipService)
			throws LogicException {
		String title = "";
		User receiver = null;
		Comment reply = comment.getReply();

		User commenter = comment.getUser();
		boolean sendTip;
		if (reply != null) {
			sendTip = !reply.getUser().equals(commenter);
		} else {
			sendTip = !scopeUser.equals(commenter);
		}
		if (!sendTip) {
			return;
		}
		Locale locale = LocaleContextHolder.getLocale();
		if (reply != null) {
			if (comment.getIsAnonymous()) {
				title = messageSource.getMessage(
						"commentHandler.anonymous.reply", new Object[] {},
						locale);
			} else {
				title = messageSource.getMessage("commentHandler.user.reply",
						new Object[] { commenter.getNickname() }, locale);
			}
			receiver = reply.getUser();
		} else {
			if (comment.getIsAnonymous()) {
				title = messageSource.getMessage(
						"commentHandler.anonymous.comment", new Object[] {},
						locale);
			} else {
				title = messageSource.getMessage("commentHandler.user.comment",
						new Object[] { commenter.getNickname() }, locale);
			}
			receiver = scopeUser;
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", title);
		model.put("comment", comment);
		Space space = scopeUser.getSpace();
		if (space == null) {
			space = spaceServer.getSpaceByUser(scopeUser);
		}
		model.put("space", space);
		String content = freeMarkers
				.processTemplateIntoString("tip/comment_blog.ftl", model);

		TipMessage message = new TipMessage();
		message.setTitle(title);
		message.setContent(content);
		message.setReceiver(receiver);
		message.setSender(userServer.getMessagers().get(0));

		tipService.sendTip(message);
	}

}
