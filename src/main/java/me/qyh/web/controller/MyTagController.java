package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.pageparam.UserTagPageParam;
import me.qyh.security.UserContext;
import me.qyh.service.TagService;

@RequestMapping("my/tag")
@Controller
public class MyTagController extends BaseController {

	@Autowired
	private TagService tagService;
	@Value("${config.pagesize.userTag}")
	private int [] pageSizes;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	@ResponseBody
	public Info list(@PathVariable("currentPage") int currentPage, UserTagPageParam param) {
		param.setCurrentPage(currentPage);
		checkPageSize(pageSizes, param);
		param.setUser(UserContext.getUser());
		param.validate();
		return new Info(true, tagService.findUserTags(param));
	}

}
