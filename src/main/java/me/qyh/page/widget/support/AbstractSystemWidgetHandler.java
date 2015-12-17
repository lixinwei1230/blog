package me.qyh.page.widget.support;

import me.qyh.entity.User;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.widget.SystemWidgetHandler;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.WidgetType;
import me.qyh.server.UserServer;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSystemWidgetHandler implements SystemWidgetHandler {

	protected final String CODE_CONFIG_NOT_EXISTS = "error.widget.config.notexists";

	@Autowired
	protected WebFreemarkers freeMarkers;
	@Autowired
	protected UserServer userServer;
	private Widget simpleWidget = null;
	protected Integer id;
	protected String name;

	public AbstractSystemWidgetHandler(Integer id, String name) {
		simpleWidget = new Widget();
		simpleWidget.setId(id);
		simpleWidget.setName(name);
		simpleWidget.setType(WidgetType.SYSTEM);
		this.id = id;
		this.name = name;
	}

	@Override
	public Widget getSimpleWidget() {
		return simpleWidget;
	}

	@Override
	public Widget getPreviewWidget(User user) {
		Widget widget = new Widget();

		widget.setId(id);
		widget.setName(name);
		widget.setHtml(getPreviewHtml(user));
		widget.setType(WidgetType.SYSTEM);

		return widget;
	}

	abstract String getPreviewHtml(User user);

}
