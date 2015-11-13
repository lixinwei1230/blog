package me.qyh.manage.web.controller;

import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.exception.SystemException;
import me.qyh.helper.refresh.Refresh;
import me.qyh.web.SpringContextHolder;

@Controller
@RequestMapping("manage")
public class RefreshController extends ManageBaseController {
	
	@RequestMapping(value = "refresh" , method = RequestMethod.GET)
	@ResponseBody
	public Info refresh(@RequestParam(value = "bean" , defaultValue = "") String bean){
		try {
			Object seeked = SpringContextHolder.getBean(bean);
			if(seeked instanceof Refresh){
				((Refresh) seeked).refresh();
			}
			return new Info(true);
		} catch (BeansException e) {
			return new Info(false);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(),e);
		}
	}

}
