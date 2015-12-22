package me.qyh.manage.web.controller;

import java.util.Date;

import me.qyh.bean.Info;
import me.qyh.entity.message.MessageSend;
import me.qyh.manage.service.MessageManageService;
import me.qyh.security.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("manage/message")
public class ManageMessageController extends ManageBaseController {

	@Autowired
	private Validator messageSendValidator;
	@Autowired
	private MessageManageService messageManageService;

	@InitBinder(value = "messageSend")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(messageSendValidator);
	}
	
	@RequestMapping(value = "send/global" , method = RequestMethod.POST)
	@ResponseBody
	public Info addGlobalMessage(@RequestBody @Validated MessageSend message){
		message.setSender(UserContext.getUser());
		message.setSendDate(new Date());
		messageManageService.insertGlobalMessage(message);
		return new Info(true);
	}

	@RequestMapping(value = "index" , method = RequestMethod.GET)
	public String index(){
		return "manage/message/index";
	}
}
