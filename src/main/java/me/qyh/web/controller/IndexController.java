package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.qyh.entity.blog.Blog.BlogStatus;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.service.BlogService;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private BlogService blogService;
	@Value("${config.pagesize.blog}")
	private int [] pageSizes;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap model) {
		BlogPageParam param = new BlogPageParam();
		param.setCurrentPage(1);
		param.setPageSize(pageSizes[0]);
		param.setStatus(BlogStatus.NORMAL);
		param.setRecommend(true);
		param.setIgnoreLevel(true);
		param.setDel(false);
		param.validate();
		
		model.addAttribute(PAGE, blogService.findBlogs(param));
		return "index";
	}
}
