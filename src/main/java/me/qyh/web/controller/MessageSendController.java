package me.qyh.web.controller;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import me.qyh.entity.message.MessageSend;
import me.qyh.entity.message.MessageSendDetail;
import me.qyh.entity.message.MessageType;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MessageReceivePageParam;
import me.qyh.pageparam.MessageSendPageParam;
import me.qyh.security.UserContext;
import me.qyh.service.MessageService;
import me.qyh.web.InvalidParamException;
import me.qyh.web.Webs;

@Controller
@RequestMapping(value = "my/message/send")
public class MessageSendController extends BaseController {

	@Autowired
	private Validator messageSendDetailValidator;
	@Autowired
	private MessageService messageService;
	@Value("${config.pageSize.messageSend}")
	private int pageSize;
	@Value("${config.pageSize.messageReceive}")
	private int receivePageSize;
	@Autowired
	private MessageSource messageSource;

	@InitBinder(value = "messageSendDetail")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(messageSendDetailValidator);
	}

	@RequestMapping(value = "send", method = RequestMethod.POST)
	@ResponseBody
	public Info send(@RequestBody @Validated MessageSendDetail detail, @RequestParam("validateCode") String code,
			HttpSession session, Locale locale) throws LogicException {
		if (!Webs.matchValidateCode(session, code)) {
			return new Info(false, messageSource.getMessage("error.validateCode", null, locale));
		}
		detail.setType(MessageType.PERSONAL);
		detail.setSender(UserContext.getUser());
		detail.setSendDate(new Date());
		messageService.insertMessage(detail);
		return new Info(true);
	}

	@RequestMapping(value = "{sendId}/receives/list/{currentPage}", method = RequestMethod.GET)
	@ResponseBody
	public Info list(@PathVariable(value = "currentPage") int currentPage, @PathVariable("sendId") int sendId,
			MessageReceivePageParam param) {
		param.setSend(new MessageSend(sendId));
		param.setCurrentPage(currentPage);
		param.setPageSize(receivePageSize);
		param.validate();

		return new Info(true, messageService.findMessageReceives(param));
	}

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, MessageSendPageParam param, ModelMap model) {
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setSender(UserContext.getUser());
		param.validate();

		model.addAttribute(PAGE, messageService.findMessageSends(param));

		return "my/message/sends";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String get(@PathVariable("id") int id, ModelMap model) throws DataNotFoundException {
		MessageSend message = messageService.getMessageSend(id);
		model.addAttribute("message", message);
		return "my/message/sendDetail";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam("ids") Set<Integer> ids) throws LogicException {
		validIds(ids);
		messageService.deleteMessageSends(ids);
		return new Info(true);
	}

	private void validIds(@RequestParam("ids") Set<Integer> ids) {
		if (ids.isEmpty() || ids.size() > pageSize) {
			throw new InvalidParamException();
		}
	}
}
