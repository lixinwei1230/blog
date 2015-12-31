package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import me.qyh.entity.blog.Blog;
import me.qyh.exception.LogicException;
import me.qyh.security.UserContext;
import me.qyh.service.BlogService;

@Controller
@RequestMapping("my/blog/temporary")
public class MyTemporaryBlogController {

	private static final String BLOG = "blog";

	@Autowired
	private BlogService blogService;
	@Autowired
	private Validator blogValidator;

	@InitBinder(value = "blog")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(blogValidator);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String loadTemporaryBlog(@PathVariable("id") int id, ModelMap model) throws LogicException {
		model.addAttribute(BLOG, blogService.getTemporaryBlog(id));
		return "my/blog/temporary";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Info saveTemporary(@RequestBody @Validated Blog blog) throws LogicException {
		blog.setSpace(UserContext.getSpace());

		blogService.insertOrUpdateTemporaryBlog(blog);
		return new Info(true);
	}

	@RequestMapping(value = "handle", method = RequestMethod.POST)
	@ResponseBody
	public Info handle(@RequestBody @Validated Blog blog) throws LogicException {
		blog.setSpace(UserContext.getSpace());

		blogService.handleTemporaryBlog(blog);
		return new Info(true, blog.getId());
	}

}
