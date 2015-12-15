package me.qyh.page.widget.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.bean.Scopes;
import me.qyh.dao.BlogDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.pageparam.BlogPageParam;

public class UserInfoWidgetHandler extends AbstractSimpleSystemWidgetHandler {

	@Autowired
	private BlogDao blogDao;

	public UserInfoWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public boolean doAuthencation(User current) {
		return true;
	}

	@Override
	public WidgetConfig getDefaultWidgetConfig(User current) {
		return new WidgetConfig();
	}

	@Override
	public String getWidgetHtml(WidgetConfig config, User owner, User visitor) {

		Map<String, Object> variables = new HashMap<String, Object>();

		if (owner.hasRole(RoleEnum.ROLE_SPACE)) {
			Scopes scopes = userServer.userRelationship(owner, visitor);
			Space space = owner.getSpace();

			BlogPageParam bParam = new BlogPageParam();
			bParam.setSpace(space);
			bParam.setScopes(scopes);
			variables.put("blog", blogDao.selectCount(bParam));
		}

		variables.put("user", owner);
		variables.put("widget", getSimpleWidget());

		return freeMarkers.processTemplateIntoString("page/widget/widget_userInfo.ftl", variables);
	}

	@Override
	String getPreviewHtml(User user) {
		return getWidgetHtml(null, user, user);
	}

}
