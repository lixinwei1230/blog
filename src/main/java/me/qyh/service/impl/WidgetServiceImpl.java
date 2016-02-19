package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.List;

import me.qyh.bean.Scopes;
import me.qyh.config.ConfigServer;
import me.qyh.config.PageConfig;
import me.qyh.dao.LocationWidgetDao;
import me.qyh.dao.PageDao;
import me.qyh.dao.UserWidgetConfigDao;
import me.qyh.dao.UserWidgetDao;
import me.qyh.entity.User;
import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.html.HtmlContentHandler;
import me.qyh.page.LocationWidget;
import me.qyh.page.Page;
import me.qyh.page.PageType;
import me.qyh.page.widget.SystemWidgetConfigHandler;
import me.qyh.page.widget.SystemWidgetHandler;
import me.qyh.page.widget.UserWidget;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.WidgetType;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.security.UserContext;
import me.qyh.server.UserServer;
import me.qyh.service.WidgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class WidgetServiceImpl extends BaseServiceImpl implements WidgetService {

	private List<SystemWidgetHandler> systemWidgetHandlers = new ArrayList<SystemWidgetHandler>();
	@Autowired
	private UserWidgetDao userWidgetDao;
	@Autowired
	private PageDao pageDao;
	@Autowired
	private LocationWidgetDao locationWidgetDao;
	@Autowired
	private UserWidgetConfigDao userWidgetConfigDao;
	@Autowired
	private ConfigServer configServer;
	@Autowired
	private UserServer userServer;
	@Autowired
	private HtmlContentHandler fullWidgetHtmlHandler;

	@Override
	@Transactional(readOnly = true)
	public Widget getPreviewWidget(Integer id, WidgetType type)
			throws LogicException {
		User current = userServer.getUserById(UserContext.getUser().getId());
		switch (type) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(id);
			if (!swh.doAuthencation(current)) {
				throw new BusinessAccessDeinedException();
			}
			Widget preview = swh.getPreviewWidget(current);
			preview.setHtml(fullWidgetHtmlHandler.handle(preview.getHtml()));
			return preview;
		case USER:
			UserWidget uw = loadUserWidget(id);
			super.doAuthencation(current, uw.getUser());

			PageConfig config = configServer.getPageConfig(current);
			HtmlContentHandler clean = config.getClean();
			if (clean != null) {
				uw.setHtml(clean.handle(uw.getHtml()));
			}

			return uw;
		}

		throw new LogicException("error.widget.notexists");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Widget> findWidgets(User user) {
		List<Widget> widgets = new ArrayList<Widget>();
		for (SystemWidgetHandler handler : systemWidgetHandlers) {
			if (handler.doAuthencation(user)) {
				widgets.add(handler.getSimpleWidget());
			}
		}
		widgets.addAll(userWidgetDao.selectByUser(user));
		return widgets;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void putWidget(LocationWidget lw) throws LogicException {
		Page page = pageDao.selectById(lw.getPage().getId());

		if (page == null) {
			throw new LogicException("error.page.notexists");
		}

		User current = UserContext.getUser();
		int count = locationWidgetDao.selectCountByPage(page);
		PageConfig pageConfig = configServer.getPageConfig(current);

		if (count >= pageConfig.getWidgetCountLimit(page.getType())) {
			throw new LogicException("error.page.widgets.oversize",
					pageConfig.getWidgetCountLimit(page.getType()));
		}

		if (locationWidgetDao.selectByIndex(lw.getR(), lw.getX(), lw.getY(),
				page) != null) {
			throw new LogicException("error.page.location.exists");
		}
		Widget widget = lw.getWidget();
		if (locationWidgetDao.selectByWidgetAndPage(widget, page) != null) {
			throw new LogicException("error.page.widget.exists");
		}

		WidgetConfig config = null;

		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			if (!swh.doAuthencation(current)) {
				throw new BusinessAccessDeinedException();
			}
			SystemWidgetConfigHandler configHandler = swh.getConfigHandler();
			locationWidgetDao.insert(lw);
			config = configHandler.getDefaultWidgetConfig(current);
			config.setWidget(lw);
			configHandler.storeWidgetConfig(config);
			break;
		case USER:
			locationWidgetDao.insert(lw);
			config = new WidgetConfig();
			config.setWidget(lw);
			userWidgetConfigDao.insert(config);
			break;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public WidgetConfig getConfig(Integer locationWidgetId)
			throws LogicException {
		LocationWidget lw = loadLocationWidget(locationWidgetId);

		Page page = pageDao.selectById(lw.getPage().getId());
		super.doAuthencation(UserContext.getUser(), page.getUser());

		Widget widget = lw.getWidget();
		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			return swh.getConfigHandler().getConfig(lw);
		case USER:
			return userWidgetConfigDao.selectByLocationWidget(lw);
		default:
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void removeWidget(Integer id) throws LogicException {
		LocationWidget lw = loadLocationWidget(id);

		Widget widget = lw.getWidget();
		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			swh.getConfigHandler().deleteWidgetConfig(lw);
			break;
		case USER:
			userWidgetConfigDao.deleteByLocationWidget(lw);
			break;
		}
		locationWidgetDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page getPage(PageType type, User user) throws LogicException {
		List<Page> pages = pageDao.selectByPageType(type, user);

		if (pages.isEmpty()) {
			throw new LogicException("error.page.notexists");
		}

		return getPage(pages.get(0));
	}

	private Page getPage(Page page) throws LogicException {
		User user = userServer.getUserById(page.getUser().getId());
		User current = UserContext.getUser();
		Scopes scopes = userServer.userRelationship(user, current);
		
		List<LocationWidget> widgets = locationWidgetDao.selectByPage(page);
		List<LocationWidget> results = new ArrayList<LocationWidget>();
		PageConfig config = null;
		HtmlContentHandler clean = null;
		for (LocationWidget lw : widgets) {
			Widget widget = lw.getWidget();
			switch (widget.getType()) {
			case SYSTEM:
				SystemWidgetHandler handler = getHandler(widget.getId());
				SystemWidgetConfigHandler configHandler = handler
						.getConfigHandler();
				WidgetConfig wc = configHandler.getConfig(lw);
				widget = getHandler(widget.getId()).getWidget(wc, user,
						UserContext.getUser());
				widget.setHtml(fullWidgetHtmlHandler.handle(widget.getHtml()));
				lw.setWidget(widget);
				lw.setConfig(wc);
				break;
			case USER:
				if (config == null) {
					config = configServer.getPageConfig(user);
					clean = config.getClean();
				}
				if (clean != null) {
					widget.setHtml(clean.handle(widget.getHtml()));
				}
				break;
			}
			if (scopes.hasScope(lw.getConfig().getScope())) {
				results.add(lw);
			}
		}

		page.addLocationWidgets(results.toArray(new LocationWidget[results
				.size()]));

		return page;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateConfig(WidgetConfig config) throws LogicException {
		LocationWidget lw = loadLocationWidget(config.getWidget().getId());
		Widget widget = lw.getWidget();

		WidgetConfig db = null;
		SystemWidgetHandler swh = null;
		switch (widget.getType()) {
		case SYSTEM:
			swh = getHandler(widget.getId());
			db = swh.getConfigHandler().getConfig(config.getId());
			break;
		case USER:
			db = userWidgetConfigDao.selectById(config.getId());
			if (db == null) {
				throw new LogicException("error.widget.config.notexists");
			}
			break;
		}

		if (!config.getClass().equals(db.getClass())) {
			throw new SystemException("当前挂件配置类型" + config.getClass()
					+ "和需要配置类型不匹配，" + "需要配置类型为" + db.getClass().getName());
		}

		switch (widget.getType()) {
		case SYSTEM:
			swh.getConfigHandler().updateWidgetConfig(config);
			break;
		case USER:
			userWidgetConfigDao.update(config);
			break;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserWidget> findUserWidgets(User user) {
		return userWidgetDao.selectByUser(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUserWidget(Integer id) throws LogicException {
		UserWidget uw = loadUserWidget(id);
		List<LocationWidget> references = locationWidgetDao.selectByWidget(uw);
		for (LocationWidget lw : references) {
			userWidgetConfigDao.deleteByLocationWidget(lw);
			locationWidgetDao.deleteById(lw.getId());
		}

		userWidgetDao.deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertUserWidget(UserWidget userWidget) throws LogicException {
		int count = userWidgetDao.selectCountByUser(userWidget.getUser());
		PageConfig config = configServer.getPageConfig(userWidget.getUser());
		if (count >= config.getUserWidgetLimit()) {
			throw new LogicException("error.userWidget.limit",
					config.getUserWidgetLimit());
		}
		userWidgetDao.insert(userWidget);
	}

	@Override
	@Transactional(readOnly = true)
	public UserWidget getUserWidget(Integer id) throws LogicException {
		return loadUserWidget(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateLocationWidget(LocationWidget widget, boolean wrap)
			throws LogicException {
		LocationWidget db = loadLocationWidget(widget.getId());
		Page page = pageDao.selectById(db.getPage().getId());
		// 寻找需要交换的挂件
		LocationWidget targetLocationWidget = locationWidgetDao.selectByIndex(
				widget.getR(), widget.getX(), widget.getY(), page);
		// 如果需要交换
		if (wrap) {
			if (targetLocationWidget != null) {
				targetLocationWidget.setR(db.getR());
				targetLocationWidget.setX(db.getX());
				targetLocationWidget.setY(db.getY());
				locationWidgetDao.update(targetLocationWidget);
				widget.setWidth(targetLocationWidget.getWidth());
			}
		} else {
			if (!db.equals(targetLocationWidget)
					&& targetLocationWidget != null) {
				throw new LogicException("error.page.location.exists");
			}
		}
		locationWidgetDao.update(widget);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUserWidget(UserWidget uw) throws LogicException {
		loadUserWidget(uw.getId());
		userWidgetDao.update(uw);
	}

	private UserWidget loadUserWidget(Integer id) throws LogicException {
		UserWidget widget = userWidgetDao.selectById(id);

		if (widget == null) {
			throw new LogicException("error.widget.notexists");
		}

		return widget;
	}

	private LocationWidget loadLocationWidget(Integer id) throws LogicException {
		LocationWidget lw = locationWidgetDao.selectById(id);
		if (lw == null) {
			throw new LogicException("error.widget.notexists");
		}

		return lw;
	}

	private SystemWidgetHandler getHandler(Integer id) throws LogicException {
		for (SystemWidgetHandler handler : systemWidgetHandlers) {
			if (id.equals(handler.getSimpleWidget().getId())) {

				return handler;
			}
		}
		throw new LogicException("error.widget.notexists");
	}

	public void setSystemWidgetHandlers(
			List<SystemWidgetHandler> systemWidgetHandlers) {
		this.systemWidgetHandlers = systemWidgetHandlers;
	}

}
