package me.qyh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import me.qyh.page.PageType;

@Controller
@RequestMapping("space/{spaceId}")
public class SpaceIndexController extends SpaceBaseController {

	@RequestMapping("index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/")
	public String _index() {
		return "user/index";
	}

	@Override
	protected PageType providePageType() {
		return PageType.HOMEPAGE;
	}

}
