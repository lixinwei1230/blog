package me.qyh.page.widget.support;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.entity.User;
import me.qyh.exception.DataNotFoundException;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.SystemWidget;
import me.qyh.page.widget.SystemWidgetHandler;
import me.qyh.server.UserServer;

public abstract class AbstractSystemWidgetHandler implements SystemWidgetHandler {

	protected final String CODE_CONFIG_NOT_EXISTS = "error.widget.config.notexists";

	@Autowired
	private WebFreemarkers freeMarkers;
	@Autowired
	protected UserServer userServer;
	private SystemWidget simpleWidget = null;
	protected Integer id;
	protected String name;

	public AbstractSystemWidgetHandler(Integer id, String name) {
		simpleWidget = new SystemWidget();
		simpleWidget.setId(id);
		simpleWidget.setName(name);
		this.id = id;
		this.name = name;
	}

	@Override
	public SystemWidget getWidget(LocationWidget widget, User owner, User visitor) throws DataNotFoundException {
		return getWidget(widget, freeMarkers, owner, visitor);
	}

	abstract SystemWidget getWidget(LocationWidget widget, WebFreemarkers freeMarkers, User owner, User visitor)
			throws DataNotFoundException;

	@Override
	public SystemWidget getSimpleWidget() {
		return simpleWidget;
	}

	@Override
	public SystemWidget getPreviewWidget(User user) {
		SystemWidget widget = new SystemWidget();

		widget.setId(id);
		widget.setName(name);
		widget.setHtml(getPreviewHtml(user, freeMarkers));

		return widget;
	}

	abstract String getPreviewHtml(User user, WebFreemarkers freeMarkers);

}
