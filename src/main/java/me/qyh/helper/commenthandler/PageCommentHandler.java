package me.qyh.helper.commenthandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.User;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.server.UserServer;
import me.qyh.service.CommentHandler;

public class PageCommentHandler implements CommentHandler {

	private static final String scope = "PAGE";

	@Autowired
	private WebFreemarkers freeMarkers;
	@Autowired
	private UserServer userServer;
	@Autowired
	private MessageSource messageSource;

	@Override
	public boolean match(CommentScope target) {
		return target.getScope().equalsIgnoreCase(scope);
	}

	@Override
	public void doAuthencationBeforeQuery(CommentScope target)
			throws CommentAuthencationException {
	}

	@Override
	public User getScopeUser(CommentScope target) throws LogicException {
		return userServer.getSupervisors().get(0);
	}

	@Override
	public void doAuthencationBeforeInsert(Comment comment)
			throws CommentAuthencationException, LogicException {

	}

	@Override
	public void tip(User scopeUser, Comment comment, TipServer tipService)
			throws LogicException {
		String title = "";
		Comment reply = comment.getReply();
		// 这里无需回复页面拥有着
		if (reply != null) {
			User commenter = comment.getUser();

			boolean sendTip = !reply.getUser().equals(commenter);

			if (!sendTip) {
				return;
			}

			Locale locale = LocaleContextHolder.getLocale();
			if (comment.getIsAnonymous()) {
				title = messageSource.getMessage(
						"commentHandler.anonymous.reply", new Object[] {},
						locale);
			} else {
				title = messageSource.getMessage("commentHandler.user.reply",
						new Object[] { commenter.getNickname() }, locale);
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("title", title);
			model.put("comment", comment);
			String content = freeMarkers
					.processTemplateIntoString("tip/comment_page.ftl", model);

			TipMessage message = new TipMessage();
			message.setTitle(title);
			message.setContent(content);
			message.setSender(userServer.getMessagers().get(0));
			message.setReceiver(reply.getUser());

			tipService.sendTip(message);
		}
	}
}
