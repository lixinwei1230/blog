package me.qyh.page.widget.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.bean.BlogFilesQueryBean;
import me.qyh.dao.BlogDao;
import me.qyh.entity.Role.RoleEnum;
import me.qyh.entity.User;
import me.qyh.page.widget.config.WidgetConfig;

/**
 * 博客分类归档
 * 
 * @author henry.qian
 *
 */
public class BlogCategoryFileWidgetHandler extends AbstractSimpleSystemWidgetHandler {

	@Autowired
	private BlogDao blogDao;

	public BlogCategoryFileWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public boolean doAuthencation(User current) {
		return current.hasRole(RoleEnum.ROLE_SPACE);
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
		BlogFilesQueryBean bean = new BlogFilesQueryBean();
		bean.setSpace(user.getSpace());
		bean.setScopes(userServer.userRelationship(user, visitor));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", blogDao.selectCategoryFile(bean));
		map.put("widget", super.getSimpleWidget());
		map.put("user", user);

		return freeMarkers.processTemplateIntoString("page/widget/widget_blog_file_category.ftl", map);
	}

}
