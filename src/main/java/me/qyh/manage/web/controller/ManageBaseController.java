package me.qyh.manage.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import me.qyh.web.controller.BaseController;

class ManageBaseController extends BaseController {

	@Autowired
	private Validator tipMessageValidator;

	@InitBinder(value = { "tipMessage" })
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(tipMessageValidator);
	}

}
