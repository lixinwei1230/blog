package me.qyh.manage.web.controller;

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
import me.qyh.entity.RoleEnum;
import me.qyh.entity.message.MessageSendDetail;
import me.qyh.entity.message.MessageType;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.SpaceManageService;
import me.qyh.manage.service.UserManageService;
import me.qyh.pageparam.UserPageParam;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.service.MessageService;

@Controller
@RequestMapping("manage/user")
public class ManageUserController extends ManageBaseController {

	@Autowired
	private Validator messageSendDetailValidator;
	@Autowired
	private UserManageService userService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private SpaceManageService spaceManageService;
	@Value("${config.pagesize.user}")
	private int pageSize;

	@InitBinder(value = "messageSendDetail")
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(messageSendDetailValidator);
	}

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage,
			UserPageParam param, ModelMap model) {
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.addIgnoreRole(RoleEnum.ROLE_MESSAGER, RoleEnum.ROLE_SUPERVISOR);

		model.addAttribute(PAGE, userService.findUsers(param));

		return "manage/user/list";
	}

	@RequestMapping(value = "toggle/abled", method = RequestMethod.POST)
	@ResponseBody
	public Info toggleAbled(@RequestBody @Validated TipMessage message,
			@RequestParam("id") int id) throws LogicException {
		userService.toggleUserAbled(id, message);
		return new Info(true);
	}

	@RequestMapping(value = "sendMessage", method = RequestMethod.POST)
	@ResponseBody
	public Info send(@RequestBody @Validated MessageSendDetail detail)
			throws LogicException {
		detail.setType(MessageType.SYSTEM);
		detail.setSender(UserContext.getUser());
		detail.setSendDate(new Date());
		messageService.insertMessage(detail);
		return new Info(true);
	}

	@RequestMapping(value = "space/toggle/abled", method = RequestMethod.POST)
	@ResponseBody
	public Info toggleAbled(@RequestBody @Validated TipMessage message,
			@RequestParam(value = "id", defaultValue = "") String id)
					throws LogicException {
		spaceManageService.toggleSpaceAbled(id, message);
		return new Info(true);
	}
}
