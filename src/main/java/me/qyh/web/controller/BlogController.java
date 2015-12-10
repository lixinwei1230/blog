package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.blog.BlogFrom;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.service.BlogService;
import me.qyh.web.InvalidParamException;

@Controller
@RequestMapping("blog")
public class BlogController extends BaseController {

	@Autowired
	private BlogService blogService;
	@Value("${config.pagesize.blog}")
	private int pageSize;

	@RequestMapping(value = "hit/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Info hit(@PathVariable("id") int id) {
		blogService.updateHits(id, 1);
		return new Info(true);
	}
	
	@RequestMapping(value = "{id}/around")
	@ResponseBody
	public Info aroudBlogs(BlogPageParam param,@PathVariable("id") int id){
		if(id < 0){
			throw new InvalidParamException();
		}
		param.setStatus(BlogStatus.NORMAL);
		return new Info(true,blogService.findAroundBlogs(id, param));
	}

	@RequestMapping(value = "list/{currentPage}")
	public String list(@PathVariable("currentPage") int currentPage, BlogPageParam param, ModelMap model)
			throws LogicException {

		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setStatus(BlogStatus.NORMAL);
		param.setFrom(BlogFrom.ORIGINAL);
		param.setRecommend(true);
		param.validate();

		model.addAttribute(PAGE, blogService.findBlogs(param));

		return "blog/list";
	}

}
