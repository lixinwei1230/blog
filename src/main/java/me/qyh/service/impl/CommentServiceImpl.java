package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import me.qyh.config.CommentConfig;
import me.qyh.config.ConfigServer;
import me.qyh.config.FrequencyLimit;
import me.qyh.dao.CommentDao;
import me.qyh.dao.CommentScopeDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.TipServer;
import me.qyh.service.CommentHandler;
import me.qyh.service.CommentService;

public class CommentServiceImpl extends BaseServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;
	@Autowired
	private CommentScopeDao commentScopeDao;
	@Autowired
	private ConfigServer configServer;
	@Autowired
	private HtmlContentHandler commentHtmlHandler;
	@Autowired
	private UserDao userDao;
	@Autowired
	private TipServer tipServer;
	private List<CommentHandler> handlers = new ArrayList<CommentHandler>();

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Comment insertComment(Comment comment) throws CommentAuthencationException, LogicException {
		CommentScope scope = comment.getScope();
		CommentHandler handler = getCommentHandler(scope);
		User owner;
		handler.doAuthencationBeforeInsert(comment);
		CommentScope _scope = commentScopeDao.selectByScopeAndScopeId(scope.getScope(), scope.getScopeId());
		if (_scope == null) {
			owner = handler.getScopeUser(scope);
			scope.setUser(owner);
			commentScopeDao.insert(scope);
		} else {
			scope = _scope;
			owner = scope.getUser();
			comment.setScope(scope);
		}
		CommentConfig config = configServer.getCommentConfig(scope);
		checkFrequencyLimit(config.getLimit(), comment.getUser());

		if (!config.getAllowAnonymous() && comment.getIsAnonymous()) {
			throw new CommentAuthencationException("error.comment.allowAnonymous");
		}
		Comment parent = comment.getParent();
		if (parent != null) {
			parent = commentDao.selectById(parent.getId());
			if (parent == null) {
				throw new LogicException("error.comment.parentNotFound");
			}
			if (parent.getParent() != null) {
				throw new SystemException("当前评论不能作为父评论，因为当前评论的父评论还有父评论");
			}
			if (!parent.getScope().equals(scope)) {
				throw new SystemException("父评论所在的评论域与当前评论域不符合");
			}
			Comment reply = commentDao.selectById(comment.getReply().getId());
			if (reply == null) {
				throw new LogicException("error.comment.replyNotFound");
			}
			if (!reply.getScope().equals(scope)) {
				throw new SystemException("被回复评论所在的评论域与当前评论域不符合");
			}
			comment.setReply(reply);
			comment.setReplyTo(reply.getIsAnonymous() ? null : reply.getUser());
			// 只能回复作者
			// 如果当前用户不是scope拥有者，如果回复对象不是scope拥有者
			if (config.getCommentOnAuthorOnly()
					&& (!(UserContext.getUser().equals(owner)) && !(reply.getUser().equals(owner)))) {
				throw new CommentAuthencationException("error.comment.commentOnAuthorOnly");
			}
		}

		commentDao.insert(comment);

		handler.tip(owner, comment, tipServer);

		Comment inserted = cleanComment(commentDao.selectById(comment.getId()), config.getAllowHtml());
		User _user = inserted.getUser();
		_user.setAvatar(userDao.selectAvatar(_user.getId()));
		return inserted;
	}

	protected void checkFrequencyLimit(FrequencyLimit limit, User user) throws LogicException {
		if (limit != null) {
			int count = commentDao.selectCountByDate(limit.getBegin(), limit.getEnd(), user);

			if (count >= limit.getLimit()) {
				throw new LogicException("error.frequency.limit.comment");
			}
		}
	}

	private Comment cleanComment(Comment comment, boolean allowHtml) {
		String content = comment.getContent();
		comment.setContent(allowHtml ? commentHtmlHandler.handle(content) : HtmlUtils.htmlEscape(content));
		return comment;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Comment> findComments(CommentPageParam param) throws CommentAuthencationException, LogicException {
		CommentScope scope = param.getScope();
		CommentScope _scope = commentScopeDao.selectByScopeAndScopeId(scope.getScope(), scope.getScopeId());
		if (_scope == null) {
			return new Page<Comment>(param, 0, new ArrayList<Comment>());
		}
		param.setScope(_scope);
		getCommentHandler(_scope).doAuthencationBeforeQuery(_scope);
		CommentConfig config = configServer.getCommentConfig(_scope);
		Page<Comment> page = _findComments(param, config);
		if (param.getParent() == null) {
			List<Comment> datas = page.getDatas();
			if (!datas.isEmpty()) {
				for (Comment comment : datas) {
					param.setCurrentPage(1);
					param.setParent(comment);
					comment.setPage(_findComments(param, config));
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteComment(Integer id) throws LogicException {
		Comment comment = commentDao.selectById(id);
		if (comment == null) {
			throw new LogicException("error.comment.notFound");
		}

		User current = UserContext.getUser();
		User owner = comment.getUser();
		boolean hasParent = (comment.getParent() != null);

		CommentScope scope = commentScopeDao.selectById(comment.getScope().getId());
		User scopeOwner = scope.getUser();
		// 如果当前用户不是评论域拥有人、回复所有人或者评论所有人,或者超级管理员
		if (!current.equals(scopeOwner) && !current.equals(owner) && !current.hasRole(RoleEnum.ROLE_SUPERVISOR)) {
			boolean hasPermission = false;
			if (hasParent) {
				Comment parent = commentDao.selectById(comment.getParent().getId());
				User parentOwner = parent.getUser();
				hasPermission = current.equals(parentOwner);
			}
			if (!hasPermission) {
				throw new BusinessAccessDeinedException();
			}
		}
		if (!hasParent) {
			commentDao.deleteByParent(comment);
		}
		commentDao.deleteById(id);
	}

	private CommentHandler getCommentHandler(CommentScope scope) {
		for (CommentHandler handler : handlers) {
			if (handler.match(scope)) {
				return handler;
			}
		}
		throw new SystemException("无法找到评论域" + scope.toString() + "的处理器");
	}

	private Page<Comment> _findComments(CommentPageParam param, CommentConfig config) {
		int total = commentDao.selectCount(param);
		List<Comment> datas = commentDao.selectPage(param);
		if (!datas.isEmpty()) {
			for (Comment comment : datas) {
				User user = comment.getUser();
				user.setAvatar(userDao.selectAvatar(user.getId()));
				cleanComment(comment, config.getAllowHtml());
			}
		}
		return new Page<Comment>(param, total, datas);
	}

	public void setHandlers(List<CommentHandler> handlers) {
		this.handlers = handlers;
	}

}
