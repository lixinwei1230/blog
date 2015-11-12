package me.qyh.web.controller;

import me.qyh.entity.blog.BlogStatus;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private BlogService blogService;
	@Value("${config.pagesize.blog}")
	private int pageSize;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "forward:blog/list/1";
	}

	@RequestMapping(value = "blog/list/{currentPage}", method = RequestMethod.GET)
	public String index(@PathVariable("currentPage") int currentPage, BlogPageParam param, ModelMap model) {
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setStatus(BlogStatus.NORMAL);
		param.setRecommend(true);
		param.validate();

		model.addAttribute(PAGE, blogService.findBlogs(param));

		return "index";
	}

}
