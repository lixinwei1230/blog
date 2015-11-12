package me.qyh.page.widget.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.bean.Scopes;
import me.qyh.dao.BlogDao;
import me.qyh.dao.BlogWidgetConfigDao;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidget;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.page.widget.config.support.BlogWidgetConfig;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.server.SpaceServer;

public class BlogWidgetHandler extends AbstractSystemWidgetHandler {

	@Autowired
	private BlogDao blogDao;
	@Autowired
	private BlogWidgetConfigDao blogWidgetConfigDao;
	@Autowired
	private SpaceServer spaceServer;
	@Value("${config.pagesize.blog}")
	private int pageSize;

	public BlogWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public void storeWidgetConfig(WidgetConfig config) throws LogicException {

		blogWidgetConfigDao.insert((BlogWidgetConfig) config);
	}

	@Override
	public boolean doAuthencation(User current) {
		return true;
	}

	@Override
	public WidgetConfig getDefaultWidgetConfig(User current) {
		BlogWidgetConfig config = new BlogWidgetConfig();
		config.setHidden(false);
		config.setSpace(current.getSpace());
		return config;
	}

	@Override
	String getPreviewHtml(User user, WebFreemarkers freeMarkers) {
		BlogWidgetConfig config = (BlogWidgetConfig) getDefaultWidgetConfig(user);

		List<Blog> blogs = blogDao.selectPage(buildParam(config, user, user));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blogs", blogs);
		map.put("widget", super.getSimpleWidget());
		map.put("config", config);

		return freeMarkers.processTemplateIntoString("page/widget/widget_blog_preview.ftl", map);
	}

	protected BlogPageParam buildParam(BlogWidgetConfig config, User owner, User visitor) {
		BlogPageParam param = new BlogPageParam();
		param.setCurrentPage(1);
		param.setPageSize(pageSize);

		Space configSpace = config.getSpace();
		param.setSpace(configSpace);

		if (configSpace == null) {
			param.setScopes(Scopes.PUBLIC);
		} else {
			if (owner.equals(visitor)) {
				param.setScopes(spaceServer.getScopes(owner, config.getSpace()));
			} else {
				param.setScopes(Scopes.PUBLIC);
			}
		}

		return param;
	}

	@Override
	SystemWidget getWidget(LocationWidget widget, WebFreemarkers freeMarkers, User owner, User visitor)
			throws DataNotFoundException {
		BlogWidgetConfig config = blogWidgetConfigDao.selectByLocationWidget(widget);
		if (config == null) {
			throw new DataNotFoundException(CODE_CONFIG_NOT_EXISTS);
		}
		SystemWidget sw = new SystemWidget();
		sw.setId(id);
		sw.setName(name);

		List<Blog> blogs = blogDao.selectPage(buildParam(config, owner, visitor));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blogs", blogs);
		map.put("widget", super.getSimpleWidget());
		map.put("config", config);

		sw.setHtml(freeMarkers.processTemplateIntoString("page/widget/widget_blog.ftl", map));
		sw.setConfig(config);

		return sw;
	}

	@Override
	public void deleteWidgetConfig(LocationWidget widget) throws LogicException {
		blogWidgetConfigDao.deleteByLocationWidget(widget);
	}

	@Override
	public WidgetConfig getConfig(LocationWidget widget) {

		BlogWidgetConfig config = blogWidgetConfigDao.selectByLocationWidget(widget);

		if (config == null) {
			config = new BlogWidgetConfig();
		}

		return config;
	}

	@Override
	public void updateWidgetConfig(WidgetConfig config) throws LogicException {
		blogWidgetConfigDao.update((BlogWidgetConfig) config);
	}

	@Override
	public WidgetConfig getConfig(Integer id) throws DataNotFoundException {
		BlogWidgetConfig db = blogWidgetConfigDao.selectById(id);
		if (db == null) {
			throw new DataNotFoundException("error.widget.config.blogWidgetConfig.notexists");
		}

		return db;
	}

}
