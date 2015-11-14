package me.qyh.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.exception.LogicException;
import me.qyh.page.PageType;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.service.BlogService;

@Controller
@RequestMapping("space/{spaceId}/blog")
public class SpaceBlogController extends SpaceBaseController {

	private static final String BLOG = "blog";
	private static final String BLOG_CATEGORYS = "categorys";

	@Value("${config.pagesize.blog}")
	private int pageSize;
	@Autowired
	private BlogService blogService;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, BlogPageParam param, ModelMap model)
			throws LogicException {

		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setStatus(BlogStatus.NORMAL);
		param.setSpace(getSpace(model));
		param.validate();

		model.addAttribute(PAGE, blogService.findBlogs(param));

		return "user/blog/list";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") int blogId, ModelMap model) throws LogicException {
		Blog blog = blogService.getBlog(blogId);

		if (!blog.getSpace().equals(getSpace(model))) {
			return String.format("redirect:/space/%s/blog/%s", blog.getSpace().getId(), blogId);
		}

		model.addAttribute(BLOG, blog);
		return "user/blog/detail";
	}

	@Override
	protected PageType providePageType() {
		return PageType.BLOG;
	}

	@Override
	protected Map<String, Object> publicData(Space space) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(BLOG_CATEGORYS, blogService.findBlogCategorys(space));
		return data;
	}

}
