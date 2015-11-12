package me.qyh.page.widget.config;

import me.qyh.entity.Id;
import me.qyh.page.LocationWidget;

public class WidgetConfig extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean hidden;
	private LocationWidget widget;

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public LocationWidget getWidget() {
		return widget;
	}

	public void setWidget(LocationWidget widget) {
		this.widget = widget;
	}

}
