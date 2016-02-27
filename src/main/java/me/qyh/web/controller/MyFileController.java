package me.qyh.web.controller;

import org.springframework.beans.BeanUtils;
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
import me.qyh.entity.MyFile.FileStatus;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.security.UserContext;
import me.qyh.service.MyFileService;

@Controller
@RequestMapping("my/file")
public class MyFileController extends BaseController {

	private static final String INDEXS = "indexs";

	@Autowired
	private MyFileService myFileService;
	@Value("${config.pageSize.myfile}")
	private int pageSize;

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET)
	public String list(@PathVariable("currentPage") int currentPage, MyFilePageParam param, ModelMap model) {
		param.setStatus(FileStatus.NORMAL);
		param.setUser(UserContext.getUser());
		param.setShowCover(false);
		MyFilePageParam _param = new MyFilePageParam();
		BeanUtils.copyProperties(param, _param);
		model.put(INDEXS, myFileService.findIndexs(_param));

		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.validate();
		model.put(PAGE, myFileService.findMyFiles(param));
		return "my/file/index";
	}

	@RequestMapping(value = "list/{currentPage}", method = RequestMethod.GET, headers = "x-requested-with=XMLHttpRequest")
	@ResponseBody
	public Info list(@PathVariable("currentPage") int currentPage, MyFilePageParam param) {
		param.setStatus(FileStatus.NORMAL);
		param.setUser(UserContext.getUser());
		param.setCurrentPage(currentPage);
		param.setPageSize(pageSize);
		param.setShowCover(false);
		param.validate();

		return new Info(true, myFileService.findMyFiles(param));
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Info delete(@RequestParam(value = "id") int id) throws LogicException {
		myFileService.deleteMyFile(id);
		return new Info(true);
	}
}
