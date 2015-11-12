package me.qyh.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.exception.LogicException;
import me.qyh.security.UserContext;
import me.qyh.service.BlogService;

@Controller
@RequestMapping("my/blog/category")
public class MyBlogCategoryController extends BaseController {

	@Autowired
	private BlogService blogService;
	@Autowired
	private Validator blogCategoryValidator;

	@InitBinder(value = "blogCategory")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(blogCategoryValidator);
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	@ResponseBody
	public Info list() {
		return new Info(true, blogService.findBlogCategorys(UserContext.getSpace()));
	}

	@RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Info addOrUpdate(@Validated @RequestBody BlogCategory blogCategory) throws LogicException {
		blogCategory.setSpace(UserContext.getSpace());
		blogCategory.setCreateDate(new Date());
		return new Info(true, blogService.insertOrUpdateBlogCategory(blogCategory));
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam("id") int id) throws LogicException {
		blogService.deleteBlogCategory(id);

		return new Info(true);
	}

}
