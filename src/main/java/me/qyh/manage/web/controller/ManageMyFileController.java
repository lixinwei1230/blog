package me.qyh.manage.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.entity.MyFile.FileStatus;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.MyFileManageService;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.server.TipMessage;

@Controller
@RequestMapping("manage/file")
public class ManageMyFileController extends ManageBaseController {

	@Value("${config.pageSize.myfile}")
	private int [] pageSizes;
	@Autowired
	private MyFileManageService service;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, MyFilePageParam param, ModelMap model) {
		param.setStatus(FileStatus.NORMAL);
		param.setCurrentPage(currentPage);
		checkPageSize(pageSizes, param);
		param.setShowCover(false);
		param.validate();

		model.put(PAGE, service.findMyFiles(param));
		return "manage/file/index";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestBody @Validated TipMessage message, @RequestParam("id") int id) throws LogicException {
		service.deleteMyFile(id, message);
		return new Info(true);
	}

}
