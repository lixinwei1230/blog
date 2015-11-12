package me.qyh.page.widget;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.entity.Id;
import me.qyh.helper.htmlclean.JsonHtmlXssSerializer;
import me.qyh.page.widget.config.WidgetConfig;

public class Widget extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String name;
	private String html;
	private WidgetType type;
	private WidgetConfig config;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public WidgetType getType() {
		return type;
	}

	public WidgetConfig getConfig() {
		return config;
	}

	public void setConfig(WidgetConfig config) {
		this.config = config;
	}

	public Widget() {

	}

	protected Widget(WidgetType type) {
		this.type = type;
	}
}
