package me.qyh.page.widget.support;

import java.util.ArrayList;
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
import me.qyh.helper.lucene.BlogIndexHandler;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidgetConfigHandler;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.WidgetType;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.page.widget.config.support.BlogWidgetConfig;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Page;
import me.qyh.server.SpaceServer;
import me.qyh.utils.Validators;

public class BlogWidgetHandler extends AbstractSystemWidgetHandler {

	@Autowired
	private BlogWidgetConfigDao blogWidgetConfigDao;
	@Autowired
	private SpaceServer spaceServer;
	@Value("${config.pagesize.blog}")
	private int pageSize;
	@Autowired
	private BlogDao blogDao;
	@Autowired
	protected BlogIndexHandler blogIndexHandler;

	public BlogWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public boolean doAuthencation(User current) {
		return true;
	}

	@Override
	String getPreviewHtml(User user) {
		BlogWidgetConfig config = (BlogWidgetConfig) (getConfigHandler().getDefaultWidgetConfig(user));
		List<Blog> blogs = searchBlog(buildParam(config, user, user)).getDatas();
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
		param.setDel(false);

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
	public Widget getWidget(WidgetConfig config, User owner, User visitor) throws LogicException {
		BlogWidgetConfig _config = (BlogWidgetConfig) config;
		if (_config == null) {
			throw new LogicException(CODE_CONFIG_NOT_EXISTS);
		}
		Widget sw = new Widget();
		sw.setId(id);
		sw.setName(name);

		List<Blog> blogs = searchBlog(buildParam(_config, owner, visitor)).getDatas();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blogs", blogs);
		map.put("widget", super.getSimpleWidget());
		map.put("config", _config);

		sw.setHtml(freeMarkers.processTemplateIntoString("page/widget/widget_blog.ftl", map));

		sw.setType(WidgetType.SYSTEM);

		return sw;
	}

	private void setSpace(BlogWidgetConfig bc) throws LogicException {
		Space space = bc.getSpace();
		if (space == null || Validators.isEmptyOrNull(space.getId(), true)) {
			bc.setSpace(null);
		} else {
			try {
				spaceServer.getSpaceById(space.getId());
			} catch (SpaceDisabledException e) {
				throw new LogicException(e.getI18nMessage());
			}
		}
	}

	private Page<Blog> searchBlog(BlogPageParam param) {
		int count = blogDao.selectCount(param);
		List<Integer> datas = blogDao.selectPage(param);
		List<Blog> blogs = datas.isEmpty() ? new ArrayList<Blog>() : blogDao.selectByIds(datas);
		return new Page<Blog>(param, count, blogs);
	}

	@Override
	public SystemWidgetConfigHandler getConfigHandler() {
		return new SystemWidgetConfigHandler() {

			@Override
			public void storeWidgetConfig(WidgetConfig config) throws LogicException {
				BlogWidgetConfig bc = (BlogWidgetConfig) config;
				setSpace(bc);
				blogWidgetConfigDao.insert(bc);
			}

			@Override
			public WidgetConfig getDefaultWidgetConfig(User current) {
				BlogWidgetConfig config = new BlogWidgetConfig();
				config.setHidden(false);
				config.setSpace(current.getSpace());
				return config;
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
			public void deleteWidgetConfig(LocationWidget widget) throws LogicException {
				blogWidgetConfigDao.deleteByLocationWidget(widget);
			}

			@Override
			public void updateWidgetConfig(WidgetConfig config) throws LogicException {
				BlogWidgetConfig bc = (BlogWidgetConfig) config;
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
		};
	}

}
