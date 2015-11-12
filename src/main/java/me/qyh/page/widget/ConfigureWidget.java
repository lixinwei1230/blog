package me.qyh.page.widget;

import me.qyh.page.widget.config.WidgetConfig;

public class ConfigureWidget {

	private WidgetConfig config;
	private Widget widget;

	public WidgetConfig getConfig() {
		return config;
	}

	public void setConfig(WidgetConfig config) {
		this.config = config;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public ConfigureWidget(WidgetConfig config, Widget widget) {
		this.config = config;
		this.widget = widget;
	}

}
