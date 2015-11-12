package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.List;

import me.qyh.config.ConfigServer;
import me.qyh.config.PageConfig;
import me.qyh.dao.LocationWidgetDao;
import me.qyh.dao.PageDao;
import me.qyh.dao.UserWidgetConfigDao;
import me.qyh.dao.UserWidgetDao;
import me.qyh.entity.User;
import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.page.LocationWidget;
import me.qyh.page.Page;
import me.qyh.page.PageType;
import me.qyh.page.widget.SystemWidget;
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
	private HtmlContentHandler fullWidgetHtmlClean;

	@Override
	@Transactional(readOnly = true)
	public Widget getPreviewWidget(Integer id, WidgetType type) throws DataNotFoundException {
		User current = UserContext.getUser();
		switch (type) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(id);
			if (!swh.doAuthencation(current)) {
				throw new BusinessAccessDeinedException();
			}
			Widget preview = swh.getPreviewWidget(current);
			preview.setHtml(fullWidgetHtmlClean.handle(preview.getHtml()));
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

		throw new DataNotFoundException("error.widget.notexists");
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
		super.doAuthencation(current, page.getUser());

		int count = locationWidgetDao.selectCountByPage(page);
		PageConfig pageConfig = configServer.getPageConfig(current);

		if (count >= pageConfig.getWidgetCountLimit(page.getType())) {
			throw new LogicException("error.page.widgets.oversize", pageConfig.getWidgetCountLimit(page.getType()));
		}

		if (locationWidgetDao.selectByIndex(lw.getR(), lw.getX(), lw.getY(), page) != null) {
			throw new LogicException("error.page.location.exists");
		}

		Widget widget = lw.getWidget();

		if (locationWidgetDao.selectByWidgetAndPage(widget, page) != null) {
			throw new LogicException("error.page.widget.exists");
		}

		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			if (!swh.doAuthencation(current)) {
				throw new BusinessAccessDeinedException();
			}
			locationWidgetDao.insert(lw);

			WidgetConfig config = swh.getDefaultWidgetConfig(current);
			config.setWidget(lw);
			swh.storeWidgetConfig(config);
			break;
		case USER:
			locationWidgetDao.insert(lw);

			WidgetConfig userConfig = new WidgetConfig();
			userConfig.setWidget(lw);
			userWidgetConfigDao.insert(userConfig);
			break;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public WidgetConfig getConfig(Integer locationWidgetId) throws DataNotFoundException {
		LocationWidget lw = loadLocationWidget(locationWidgetId);

		Page page = pageDao.selectById(lw.getPage().getId());
		super.doAuthencation(UserContext.getUser(), page.getUser());

		Widget widget = lw.getWidget();

		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			return swh.getConfig(lw);
		case USER:
			return userWidgetConfigDao.selectByLocationWidget(lw);
		}

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void removeWidget(Integer id) throws LogicException {
		LocationWidget lw = loadLocationWidget(id);
		Page page = pageDao.selectById(lw.getPage().getId());
		super.doAuthencation(UserContext.getUser(), page.getUser());

		Widget widget = lw.getWidget();
		switch (widget.getType()) {
		case SYSTEM:
			SystemWidgetHandler swh = getHandler(widget.getId());
			swh.deleteWidgetConfig(lw);
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
			throw new DataNotFoundException("error.page.notexists");
		}

		return getPage(pages.get(0));
	}

	private Page getPage(Page page) throws LogicException {
		User user = userServer.getUserById(page.getUser().getId());

		List<LocationWidget> widgets = locationWidgetDao.selectByPage(page);
		PageConfig config = null;
		HtmlContentHandler clean = null;
		for (LocationWidget lw : widgets) {
			Widget widget = lw.getWidget();
			switch (widget.getType()) {
			case SYSTEM:
				SystemWidget sw = getHandler(widget.getId()).getWidget(lw, user, UserContext.getUser());
				sw.setHtml(fullWidgetHtmlClean.handle(sw.getHtml()));
				lw.setWidget(sw);
				break;
			case USER:
				UserWidget userWidget = loadUserWidget(widget.getId());
				userWidget.setConfig(userWidgetConfigDao.selectByLocationWidget(lw));
				if (config == null) {
					config = configServer.getPageConfig(user);
					clean = config.getClean();
				}
				if (clean != null) {
					userWidget.setHtml(clean.handle(userWidget.getHtml()));
				}
				lw.setWidget(userWidget);
				break;
			}
		}

		page.addLocationWidgets(widgets.toArray(new LocationWidget[widgets.size()]));

		return page;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateConfig(WidgetConfig config) throws LogicException {
		LocationWidget lw = loadLocationWidget(config.getWidget().getId());
		Page page = pageDao.selectById(lw.getPage().getId());
		super.doAuthencation(UserContext.getUser(), page.getUser());

		Widget widget = lw.getWidget();

		WidgetConfig db = null;
		SystemWidgetHandler swh = null;
		switch (widget.getType()) {
		case SYSTEM:
			swh = getHandler(widget.getId());
			db = swh.getConfig(config.getId());
			break;
		case USER:
			db = userWidgetConfigDao.selectById(config.getId());
			if (db == null) {
				throw new DataNotFoundException("error.widget.config.notexists");
			}
			break;
		}

		if (!config.getClass().equals(db.getClass())) {
			throw new SystemException(
					"当前挂件配置类型" + config.getClass() + "和需要配置类型不匹配，" + "需要配置类型为" + db.getClass().getName());
		}

		LocationWidget _lw = loadLocationWidget(db.getWidget().getId());
		Page _page = pageDao.selectById(_lw.getPage().getId());
		super.doAuthencation(UserContext.getUser(), _page.getUser());

		switch (widget.getType()) {
		case SYSTEM:
			swh.updateWidgetConfig(config);
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

		super.doAuthencation(uw.getUser(), UserContext.getUser());

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
			throw new LogicException("error.userWidget.limit", config.getUserWidgetLimit());
		}
		userWidgetDao.insert(userWidget);
	}

	@Override
	@Transactional(readOnly = true)
	public UserWidget getUserWidget(Integer id) throws DataNotFoundException {
		return loadUserWidget(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateLocationWidget(LocationWidget widget, boolean wrap) throws LogicException {
		LocationWidget db = loadLocationWidget(widget.getId());
		Page page = pageDao.selectById(db.getPage().getId());
		super.doAuthencation(UserContext.getUser(), page.getUser());

		// 寻找需要交换的挂件
		LocationWidget targetLocationWidget = locationWidgetDao.selectByIndex(widget.getR(), widget.getX(),
				widget.getY(), page);
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
			if (!db.equals(targetLocationWidget) && targetLocationWidget != null) {
				throw new LogicException("error.page.location.exists");
			}
		}
		locationWidgetDao.update(widget);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUserWidget(UserWidget uw) throws LogicException {
		UserWidget db = loadUserWidget(uw.getId());
		super.doAuthencation(UserContext.getUser(), db.getUser());

		userWidgetDao.update(uw);
	}

	private UserWidget loadUserWidget(Integer id) throws DataNotFoundException {
		UserWidget widget = userWidgetDao.selectById(id);

		if (widget == null) {
			throw new DataNotFoundException("error.widget.notexists");
		}

		return widget;
	}

	private LocationWidget loadLocationWidget(Integer id) throws DataNotFoundException {
		LocationWidget lw = locationWidgetDao.selectById(id);
		if (lw == null) {
			throw new DataNotFoundException("error.widget.notexists");
		}

		return lw;
	}

	private SystemWidgetHandler getHandler(Integer id) throws DataNotFoundException {
		for (SystemWidgetHandler handler : systemWidgetHandlers) {
			if (id.equals(handler.getSimpleWidget().getId())) {

				return handler;
			}
		}
		throw new DataNotFoundException("error.widget.notexists");
	}

	public void setSystemWidgetHandlers(List<SystemWidgetHandler> systemWidgetHandlers) {
		this.systemWidgetHandlers = systemWidgetHandlers;
	}
}
