package me.qyh.page.widget.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.bean.BlogFilesQueryBean;
import me.qyh.bean.DateFileIndex;
import me.qyh.bean.DateFileIndexs;
import me.qyh.dao.BlogDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.widget.config.WidgetConfig;

/**
 * 博客分类归档
 * 
 * @author henry.qian
 *
 */
public class BlogDateFileWidgetHandler extends AbstractSimpleSystemWidgetHandler {

	@Autowired
	private BlogDao blogDao;

	public BlogDateFileWidgetHandler(Integer id, String name) {
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
	String getPreviewHtml(User user, WebFreemarkers freeMarkers) {
		return parseHtml(user, user, freeMarkers);
	}

	@Override
	String getWidgetHtml(WidgetConfig config, User owner, User visitor, WebFreemarkers freeMarkers) {
		return parseHtml(owner, visitor, freeMarkers);
	}

	protected String parseHtml(User user, User visitor, WebFreemarkers freeMarkers) {
		BlogFilesQueryBean bean = new BlogFilesQueryBean();
		bean.setSpace(user.getSpace());
		bean.setScopes(userServer.userRelationship(user, visitor));

		List<DateFileIndex> files = blogDao.selectDateFile(bean);
		List<DateFileIndexs> _files = DateFileIndexs.buildYM(files);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", _files == null ? Collections.emptyList() : _files);
		map.put("widget", super.getSimpleWidget());
		map.put("user", user);

		return freeMarkers.processTemplateIntoString("page/widget/widget_blog_file_date.ftl", map);
	}

}
