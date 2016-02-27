package me.qyh.page.widget;

import me.qyh.entity.Id;
import me.qyh.helper.html.JsonHtmlXssSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Widget extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String name;
	private String html;
	private WidgetType type;

	public enum WidgetType {
		SYSTEM, // 系统
		USER;// 用户
	}

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

	public Widget() {

	}

	public WidgetType getType() {
		return type;
	}

	public void setType(WidgetType type) {
		this.type = type;
	}

}
