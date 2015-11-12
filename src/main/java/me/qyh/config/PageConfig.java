package me.qyh.config;

import java.util.HashMap;
import java.util.Map;

import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.page.PageType;

public class PageConfig {

	private int userWidgetLimit;
	private Map<PageType, Integer> widgetCountLimits = new HashMap<PageType, Integer>();
	private HtmlContentHandler clean;

	public int getUserWidgetLimit() {
		return userWidgetLimit;
	}

	public void setUserWidgetLimit(int userWidgetLimit) {
		this.userWidgetLimit = userWidgetLimit;
	}

	public void addWidgetCountLimits(PageType type, int maxWidget) {
		widgetCountLimits.put(type, maxWidget);
	}

	public int getWidgetCountLimit(PageType type) {
		return widgetCountLimits.get(type);
	}

	public HtmlContentHandler getClean() {
		return clean;
	}

	public void setClean(HtmlContentHandler clean) {
		this.clean = clean;
	}
}
