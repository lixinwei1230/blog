package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.service.CommentService;

@Controller
@RequestMapping("comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	@Value("${config.pagesize.comment}")
	private int pageSize;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	@ResponseBody
	public Info list(@PathVariable("currentPage") int currentPage, CommentPageParam param) throws LogicException {
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.validate();
		return new Info(true, commentService.findComments(param));
	}

}
