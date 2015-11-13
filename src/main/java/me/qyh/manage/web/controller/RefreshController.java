package me.qyh.manage.web.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.qyh.bean.Info;
import me.qyh.exception.SystemException;
import me.qyh.helper.refresh.Refresh;

@Controller
@RequestMapping("manage/refresh")
public class RefreshController extends ManageBaseController implements ApplicationContextAware {

	private static final String REFRESHS = "refreshs";
	private ApplicationContext ctx;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		try {
			model.addAttribute(REFRESHS, new ArrayList<String>(allRefresh().keySet()));
			return "manage/refresh";
		} catch (BeansException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	private Map<String, Refresh> allRefresh() {
		Map<String, Refresh> refreshs = ctx.getBeansOfType(Refresh.class);
		ApplicationContext parent = ctx.getParent();
		if (parent != null) {
			refreshs.putAll(parent.getBeansOfType(Refresh.class));
		}
		return refreshs;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Info refresh(@RequestParam("bean") String bean) {
		if ("&&all".equals(bean)) {
			try {
				Map<String, Refresh> all = allRefresh();
				for (Map.Entry<String, Refresh> it : all.entrySet()) {
					it.getValue().refresh();
				}
				return new Info(true);
			} catch (Exception e) {
				throw new SystemException(e);
			}
		} else {
			try {
				Object seeked = ctx.getBean(bean);
				if (seeked instanceof Refresh) {
					((Refresh) seeked).refresh();
					return new Info(true);
				}
				return new Info(false);
			} catch (BeansException e) {
				return new Info(false);
			} catch (Exception e) {
				throw new SystemException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

}
