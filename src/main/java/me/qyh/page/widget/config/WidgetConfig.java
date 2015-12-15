package me.qyh.page.widget.config;

import me.qyh.entity.Id;
import me.qyh.entity.Scope;
import me.qyh.page.LocationWidget;

public class WidgetConfig extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean hidden;
	private LocationWidget widget;
	private Scope scope = Scope.PUBLIC;

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

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}
}
