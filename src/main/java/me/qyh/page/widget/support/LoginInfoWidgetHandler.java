package me.qyh.page.widget.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.dao.LoginInfoDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.pageparam.LoginInfoPageParam;

/**
 * 用户登录信息
 * 
 * @author henry.qian
 *
 */
public class LoginInfoWidgetHandler extends AbstractSimpleSystemWidgetHandler {

	@Autowired
	private LoginInfoDao loginInfoDao;
	@Value("${config.widget.loginInfoSize}")
	private int loginInfoSize;

	public LoginInfoWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public boolean doAuthencation(User current) {
		return current.hasRole(RoleEnum.ROLE_USER);
	}

	@Override
	public WidgetConfig getDefaultWidgetConfig(User current) {
		return new WidgetConfig();
	}

	@Override
	String getPreviewHtml(User user) {
		return parseHtml(user, user);
	}

	@Override
	String getWidgetHtml(WidgetConfig config, User owner, User visitor) {
		return parseHtml(owner, visitor);
	}

	protected String parseHtml(User user, User visitor) {
		
		LoginInfoPageParam param = new LoginInfoPageParam();
		param.setCurrentPage(1);
		param.setUser(user);
		param.setPageSize(loginInfoSize);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loginInfos", loginInfoDao.selectPage(param));
		map.put("widget", super.getSimpleWidget());
		map.put("user", user);

		return freeMarkers.processTemplateIntoString("page/widget/login_info.ftl", map);
	}

}
