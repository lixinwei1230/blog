package me.qyh.page;

import me.qyh.entity.Id;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.config.WidgetConfig;

public class LocationWidget extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int x;
	private int y;
	private int r;
	private Widget widget;
	private Page page;
	private int width;
	private WidgetConfig config;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public WidgetConfig getConfig() {
		return config;
	}

	public void setConfig(WidgetConfig config) {
		this.config = config;
	}
}
