package me.qyh.config;

import java.util.HashMap;
import java.util.Map;

import me.qyh.helper.html.HtmlContentHandler;
import me.qyh.page.PageType;

public class PageConfig {

	private int userWidgetLimit;
	private Map<PageType, Integer> widgetCountLimits = new HashMap<PageType, Integer>();
	private HtmlContentHandler clean;

	public int getUserWidgetLimit() {
		return userWidgetLimit;
	}

	void addWidgetCountLimits(PageType type, int maxWidget) {
		widgetCountLimits.put(type, maxWidget);
	}

	public int getWidgetCountLimit(PageType type) {
		return widgetCountLimits.get(type);
	}

	public HtmlContentHandler getClean() {
		return clean;
	}

	PageConfig(int userWidgetLimit, HtmlContentHandler clean) {
		super();
		this.userWidgetLimit = userWidgetLimit;
		this.clean = clean;
	}
}
