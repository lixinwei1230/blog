package me.qyh.helper.commenthandler;

import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.User;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.server.TipServer;

public interface CommentHandler {

	User getScopeUser(CommentScope target) throws LogicException;

	boolean match(CommentScope target);

	void doAuthencationBeforeInsert(Comment comment) throws CommentAuthencationException, LogicException;

	void doAuthencationBeforeQuery(CommentScope target) throws CommentAuthencationException, LogicException;

	void tip(User scopeUser, Comment comment, TipServer tipService) throws LogicException;

}
