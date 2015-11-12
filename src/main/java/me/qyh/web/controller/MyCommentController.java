package me.qyh.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.Comment;
import me.qyh.exception.CommentAuthencationException;
import me.qyh.exception.LogicException;
import me.qyh.security.UserContext;
import me.qyh.service.CommentService;

@Controller
@RequestMapping("my/comment")
public class MyCommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private Validator commentValidator;

	@InitBinder(value = "comment")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(commentValidator);
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Info send(@Validated @RequestBody Comment comment)
			throws CommentAuthencationException, LogicException {
		comment.setUser(UserContext.getUser());
		comment.setCommentDate(new Date());
		return new Info(true, commentService.insertComment(comment));
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@PathVariable("id") int id) throws LogicException {
		commentService.deleteComment(id);
		return new Info(true);
	}

}
