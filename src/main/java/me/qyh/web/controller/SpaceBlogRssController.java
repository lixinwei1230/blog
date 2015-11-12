package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;
import me.qyh.service.BlogService;
import me.qyh.web.rss.SpaceBlogRssFeedView;

@Controller
@RequestMapping("space/{spaceId}/blog")
public class SpaceBlogRssController extends SpaceBaseController {

	public static final String BLOGS = "blogs";

	@Autowired
	private SpaceBlogRssFeedView view;
	@Autowired
	private BlogService blogService;
	@Value("${config.pagesize.blog}")
	private int pageSize;

	@RequestMapping(value = "rss", method = RequestMethod.GET)
	public ModelAndView rss(ModelMap model, BlogPageParam param) {
		ModelAndView mav = new ModelAndView();
		param.setIgnoreLevel(true);
		param.setSpace(super.getSpace(mav.getModelMap()));
		param.setPageSize(pageSize);
		param.setCurrentPage(1);

		param.validate();

		Page<Blog> page = blogService.findBlogs(param);
		mav.addObject(BLOGS, page.getDatas());
		mav.setView(view);

		return mav;
	}
}
