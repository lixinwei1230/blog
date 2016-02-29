package me.qyh.web.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.message.MessageReceive;
import me.qyh.entity.message.MessageReceive.MessageStatus;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MessageReceivePageParam;
import me.qyh.security.UserContext;
import me.qyh.service.MessageService;
import me.qyh.web.InvalidParamException;

@Controller
@RequestMapping(value = "my/message/receive")
public class MessageReceiveController extends BaseController {

	@Autowired
	private MessageService messageService;
	@Value("${config.pageSize.messageReceive}")
	private int [] pageSizes;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable(value = "currentPage") int currentPage, MessageReceivePageParam param,
			ModelMap model) {
		param.setCurrentPage(currentPage);
		checkPageSize(pageSizes, param);
		param.setReceiver(UserContext.getUser());
		param.validate();

		model.addAttribute(PAGE, messageService.findMessageReceives(param));

		return "my/message/receives";
	}

	@RequestMapping(value = "getToReadMessageCount", method = RequestMethod.GET)
	@ResponseBody
	public Info getToReadMessageCount() {
		int count = messageService.getToReadMessageCount(UserContext.getUser());
		return new Info(true, count);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String getMessage(@PathVariable(value = "id") int id, ModelMap model) throws LogicException {
		MessageReceive receive = messageService.getMessageReceive(id);
		model.addAttribute("receive", receive);
		return "my/message/receiveDetail";
	}

	@RequestMapping(value = "read", method = RequestMethod.POST)
	@ResponseBody
	public Info read(@RequestParam(value = "ids") Set<Integer> ids) throws LogicException {
		validIds(ids);
		messageService.updateIsRead(ids, true);
		return new Info(true);
	}

	@RequestMapping(value = "unread", method = RequestMethod.POST)
	@ResponseBody
	public Info unread(@RequestParam(value = "ids") Set<Integer> ids) throws LogicException {
		validIds(ids);
		messageService.updateIsRead(ids, false);
		return new Info(true);
	}

	@RequestMapping(value = "status/change", method = RequestMethod.POST)
	@ResponseBody
	public Info changeStatus(@RequestParam(value = "ids") Set<Integer> ids,
			@RequestParam(value = "status", defaultValue = "COMMON") MessageStatus status) throws LogicException {
		validIds(ids);
		messageService.updateMessageRecieveStatus(ids, status);
		return new Info(true);
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam("ids") Set<Integer> ids) throws LogicException {
		validIds(ids);
		messageService.deleteMessageReceives(ids);
		return new Info(true);
	}

	private void validIds(@RequestParam("ids") Set<Integer> ids) {
		if (ids.isEmpty() || ids.size() > pageSizes[pageSizes.length-1]) {
			throw new InvalidParamException();
		}
	}
}
