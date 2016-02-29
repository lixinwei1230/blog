package me.qyh.web.controller;

import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;
import me.qyh.service.BlogService;
import me.qyh.web.rss.SpaceBlogRssFeedView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

@Controller
@RequestMapping("space/{spaceId}/blog")
public class SpaceBlogRssController extends SpaceBaseController {

	public static final String BLOGS = "blogs";

	@Autowired
	private SpaceBlogRssFeedView view;
	@Autowired
	private BlogService blogService;
	@Value("${config.pagesize.blog}")
	private int [] pageSizes;

	@RequestMapping(value = "rss", method = RequestMethod.GET)
	public View rss(ModelMap model, BlogPageParam param) {
		param.setIgnoreLevel(true);
		param.setSpace(super.getSpace(model));
		checkPageSize(pageSizes, param);
		param.setCurrentPage(1);

		param.validate();

		Page<Blog> page = blogService.findBlogs(param);
		model.put(BLOGS, page.getDatas());

		return view;
	}
}
