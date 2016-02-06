package me.qyh.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.Editor;
import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.entity.blog.TemporaryBlog;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.security.UserContext;
import me.qyh.service.BlogService;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;

@Controller
@RequestMapping("my/blog")
public class MyBlogController extends BaseController {

	private static final String CATEGORYS = "categorys";
	private static final String BLOG = "blog";
	private static final String PREVIEW = "preview";

	@Autowired
	private BlogService blogService;
	@Autowired
	private Validator blogValidator;
	@Value("${config.pagesize.blog}")
	private int pageSize;

	@InitBinder(value = "blog")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(blogValidator);
	}

	@RequestMapping(value = "write", method = RequestMethod.GET)
	public String write(ModelMap model,
			@RequestParam(value = "editor", required = false, defaultValue = "MD") Editor editor) {
		Blog blog = new Blog();
		blog.setSpace(UserContext.getSpace());
		TemporaryBlog tBlog = blogService.getTemporaryBlog(blog);
		String _editor = editor.name().toLowerCase();
		return tBlog == null ? "my/blog/write_" + _editor
				: "forward:temporary/" + tBlog.getDummyId() + "?editor=" + tBlog.getEditor().name();
	}

	@RequestMapping(value = "write", method = RequestMethod.POST)
	@ResponseBody
	public Info write(@RequestBody @Validated Blog blog) throws LogicException {
		blog.setSpace(UserContext.getSpace());

		blogService.insertBlog(blog);

		return new Info(true, blog.getId());
	}

	@RequestMapping(value = "logicDelete", method = RequestMethod.POST)
	@ResponseBody
	public Info deleteBlogLogic(@RequestParam("id") int id) throws LogicException {

		blogService.deleteBlogLogic(id);
		return new Info(true);
	}

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, ModelMap model, BlogPageParam param) {
		Space current = UserContext.getSpace();

		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setSpace(current);
		param.setStatus(BlogStatus.NORMAL);
		param.setDel(false);
		param.validate();

		model.addAttribute(PAGE, blogService.findBlogs(param));
		model.addAttribute(CATEGORYS, blogService.findBlogCategorys(current));

		return "my/blog/list";
	}

	@RequestMapping(value = "recycler/list/{currentPage}", method = RequestMethod.GET)
	public String recycler(@PathVariable("currentPage") int currentPage, ModelMap model) {
		BlogPageParam param = new BlogPageParam();
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setSpace(UserContext.getSpace());
		param.setDel(true);
		param.setStatus(null);
		param.validate();

		model.addAttribute(PAGE, blogService.findBlogs(param));
		return "my/blog/recycler";
	}

	@RequestMapping(value = "scheduled/list/{currentPage}", method = RequestMethod.GET)
	public String scheduled(@PathVariable("currentPage") int currentPage, ModelMap model) {
		BlogPageParam param = new BlogPageParam();
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setSpace(UserContext.getSpace());
		param.setStatus(BlogStatus.SCHEDULED);
		param.setDel(false);
		param.validate();
		model.addAttribute(PAGE, blogService.findBlogs(param));
		return "my/blog/scheduled";
	}

	@RequestMapping(value = "recover", method = RequestMethod.POST)
	@ResponseBody
	public Info recover(@RequestParam("id") int id) throws LogicException {
		blogService.recover(id);
		return new Info(true);
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam("id") int id) throws LogicException {
		blogService.deleteBlog(id);
		return new Info(true);
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable("id") int id, ModelMap model) throws LogicException {
		Blog blog = blogService.getBlog(id);
		model.addAttribute(BLOG, blog);
		TemporaryBlog tBlog = blogService.getTemporaryBlog(blog);
		if (tBlog != null) {
			model.addAttribute("tBlog", tBlog);
		}
		return "my/blog/update_"+blog.getEditor().name().toLowerCase();
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Info update(@RequestBody @Validated Blog blog) throws LogicException {
		blog.setLastModifyDate(new Date());
		blog.setSpace(UserContext.getSpace());
		blogService.updateBlog(blog);

		return new Info(true);
	}
	
	@RequestMapping(value = "preview", method = RequestMethod.POST)
	public String preview(@RequestParam("content") String content , ModelMap model){
		if( Validators.isEmptyOrNull(content, true) || !Strings.hasText(content)){
			throw new InvalidParamException();
		}
		model.put(PREVIEW, blogService.preview(content));
		return "my/blog/preview";
	}

}