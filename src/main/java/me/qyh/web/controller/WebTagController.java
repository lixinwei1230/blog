package me.qyh.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.pageparam.WebTagPageParam;
import me.qyh.service.TagService;

@RequestMapping("tag/web")
@Controller
public class WebTagController extends BaseController {

	@Autowired
	private TagService tagService;
	@Value("${config.pagesize.webTag}")
	private int pageSize;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	@ResponseBody
	public Info list(@PathVariable("currentPage") int currentPage,
			WebTagPageParam param) {
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.validate();
		return new Info(true, tagService.findWebTags(param));
	}

}
