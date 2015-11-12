package me.qyh.manage.service;

import me.qyh.exception.LogicException;
import me.qyh.server.TipMessage;
import me.qyh.service.BlogService;

public interface BlogManageService extends BlogService {

	void deleteBlog(int id, TipMessage message) throws LogicException;

	void toggleBlogRecommand(int id, TipMessage message) throws LogicException;

}
