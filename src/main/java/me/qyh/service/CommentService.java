package me.qyh.service;

import me.qyh.entity.Comment;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.pageparam.Page;

public interface CommentService {

	Comment insertComment(Comment comment)
			throws CommentAuthencationException, LogicException;

	Page<Comment> findComments(CommentPageParam param)
			throws CommentAuthencationException, LogicException;

	void deleteComment(Integer id) throws LogicException;

}
