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
import me.qyh.exception.LogicException;
import me.qyh.exception.SpaceDisabledException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidget;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.page.widget.config.support.BlogWidgetConfig;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.server.SpaceServer;
import me.qyh.utils.Validators;

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
		BlogWidgetConfig bc = (BlogWidgetConfig)config;
		setSpace(bc);
		blogWidgetConfigDao.insert(bc);
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
			param.setRecommend(true);
		} else {
			if (owner.equals(visitor)) {
				param.setScopes(spaceServer.getScopes(owner, configSpace));
			} else {
				param.setScopes(Scopes.PUBLIC);
			}
			param.setRecommend(null);
		}
		
		param.setIgnoreLevel(true);

		return param;
	}

	@Override
	SystemWidget getWidget(LocationWidget widget, WebFreemarkers freeMarkers, User owner, User visitor)
			throws LogicException {
		BlogWidgetConfig config = blogWidgetConfigDao.selectByLocationWidget(widget);
		if (config == null) {
			throw new LogicException(CODE_CONFIG_NOT_EXISTS);
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
		BlogWidgetConfig bc = (BlogWidgetConfig)config;
		setSpace(bc);
		blogWidgetConfigDao.update(bc);
	}

	@Override
	public WidgetConfig getConfig(Integer id) throws LogicException {
		BlogWidgetConfig db = blogWidgetConfigDao.selectById(id);
		if (db == null) {
			throw new LogicException("error.widget.config.blogWidgetConfig.notexists");
		}

		return db;
	}
	
	private void setSpace(BlogWidgetConfig bc) throws LogicException{
		Space space = bc.getSpace();
		if(space == null || Validators.isEmptyOrNull(space.getId(),true)){
			bc.setSpace(null);
		}else{
			try{
				spaceServer.getSpaceById(space.getId());
			}catch (SpaceDisabledException e) {
				throw new LogicException(e.getI18nMessage());
			}
		}
	}

}
