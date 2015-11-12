package me.qyh.page.widget.config.support;

import me.qyh.entity.Space;
import me.qyh.page.widget.config.WidgetConfig;

public class BlogWidgetConfig extends WidgetConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Space space;
	private BlogWidgetDisplayMode mode;

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public BlogWidgetDisplayMode getMode() {
		return mode;
	}

	public void setMode(BlogWidgetDisplayMode mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return super.toString() + "BlogWidgetConfig [space=" + space + "]";
	}

	public BlogWidgetConfig() {
		this.mode = BlogWidgetDisplayMode.PREVIEW;
	}

}
