package me.qyh.web.controller;

import me.qyh.pageparam.PageParam;

public class BaseController {

	// 分页对象名称
	protected final String PAGE = "page";
	protected final String SPACE = "space";
	protected final String USER = "user";
	protected final String INFO = "info";
	protected final String SUCCESS = "success";
	protected final String ERROR = "error";
	
	protected void checkPageSize(int [] allows , PageParam param){
		int size = param.getPageSize();
		for(int allow : allows){
			if(allow == size){
				return ;
			}
		}
		param.setPageSize(allows[0]);
	}

}
