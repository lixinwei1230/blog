package me.qyh.page.widget.support;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.dao.UserWidgetConfigDao;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidget;
import me.qyh.page.widget.config.WidgetConfig;

public abstract class AbstractSimpleSystemWidgetHandler extends AbstractSystemWidgetHandler {

	@Autowired
	protected UserWidgetConfigDao userWidgetConfigDao;

	public AbstractSimpleSystemWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public WidgetConfig getConfig(LocationWidget widget) {

		WidgetConfig config = userWidgetConfigDao.selectByLocationWidget(widget);

		if (config == null) {
			config = new WidgetConfig();
		}

		return config;
	}

	@Override
	public void storeWidgetConfig(WidgetConfig config) throws LogicException {
		userWidgetConfigDao.insert(config);
	}

	@Override
	public void deleteWidgetConfig(LocationWidget widget) throws LogicException {
		userWidgetConfigDao.deleteByLocationWidget(widget);
	}

	@Override
	public void updateWidgetConfig(WidgetConfig config) throws LogicException {
		userWidgetConfigDao.update(config);
	}

	@Override
	public WidgetConfig getConfig(Integer id) throws LogicException {
		WidgetConfig db = userWidgetConfigDao.selectById(id);
		if (db == null) {
			throw new LogicException(CODE_CONFIG_NOT_EXISTS);
		}
		return db;
	}

	@Override
	public SystemWidget getWidget(LocationWidget widget, User owner, User visitor) throws LogicException {
		WidgetConfig config = userWidgetConfigDao.selectByLocationWidget(widget);

		if (config == null) {
			throw new LogicException(CODE_CONFIG_NOT_EXISTS);
		}

		SystemWidget sw = new SystemWidget();
		sw.setId(id);
		sw.setName(name);
		sw.setConfig(config);
		sw.setHtml(getWidgetHtml(config, owner, visitor));

		return sw;
	}

	abstract String getWidgetHtml(WidgetConfig config, User owner, User visitor);

}
