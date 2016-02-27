package me.qyh.page.widget.support;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.dao.UserWidgetConfigDao;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidgetConfigHandler;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.Widget.WidgetType;
import me.qyh.page.widget.config.WidgetConfig;

public abstract class AbstractSimpleSystemWidgetHandler extends AbstractSystemWidgetHandler {

	@Autowired
	protected UserWidgetConfigDao userWidgetConfigDao;

	public AbstractSimpleSystemWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public Widget getWidget(WidgetConfig config, User owner, User visitor) throws LogicException {
		if (config == null) {
			throw new LogicException(CODE_CONFIG_NOT_EXISTS);
		}

		Widget sw = new Widget();
		sw.setId(id);
		sw.setName(name);
		sw.setHtml(getWidgetHtml(config, owner, visitor));
		sw.setType(WidgetType.SYSTEM);
		return sw;
	}

	abstract String getWidgetHtml(WidgetConfig config, User owner, User visitor);
	
	abstract WidgetConfig getDefaultWidgetConfig(User current);

	@Override
	public SystemWidgetConfigHandler getConfigHandler() {
		return new SystemWidgetConfigHandler() {
			
			@Override
			public void updateWidgetConfig(WidgetConfig config) throws LogicException {
				userWidgetConfigDao.update(config);
			}
			
			@Override
			public void storeWidgetConfig(WidgetConfig config) throws LogicException {
				userWidgetConfigDao.insert(config);
			}
			
			
			@Override
			public WidgetConfig getDefaultWidgetConfig(User current) {
				return AbstractSimpleSystemWidgetHandler.this.getDefaultWidgetConfig(current);
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
			public WidgetConfig getConfig(LocationWidget widget) {
				WidgetConfig config = userWidgetConfigDao.selectByLocationWidget(widget);

				if (config == null) {
					config = new WidgetConfig();
				}

				return config;
			}
			
			@Override
			public void deleteWidgetConfig(LocationWidget widget) throws LogicException {
				userWidgetConfigDao.deleteByLocationWidget(widget);
			}
		};
	}

}
