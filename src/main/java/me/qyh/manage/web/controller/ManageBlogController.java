package me.qyh.manage.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.BlogManageService;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.server.TipMessage;

@Controller
@RequestMapping("manage/blog")
public class ManageBlogController extends ManageBaseController {

	@Value("${config.pagesize.blog}")
	private int pageSize;
	@Autowired
	private BlogManageService blogService;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, ModelMap model, BlogPageParam param) {

		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setStatus(BlogStatus.NORMAL);
		param.setIgnoreLevel(true);
		param.setDel(false);

		model.addAttribute(PAGE, blogService.findBlogs(param));

		return "manage/blog/list";
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Info delete(@RequestBody @Validated TipMessage message, @RequestParam("id") int id) throws LogicException {
		blogService.deleteBlog(id, message);

		return new Info(true);
	}

	@ResponseBody
	@RequestMapping(value = "recommend", method = RequestMethod.POST)
	public Info toggleRecommend(@RequestBody @Validated TipMessage message, @RequestParam("id") int id)
			throws LogicException {
		blogService.toggleBlogRecommand(id, message);

		return new Info(true);
	}

}
