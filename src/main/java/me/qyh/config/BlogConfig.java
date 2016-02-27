package me.qyh.config;

import me.qyh.helper.html.HtmlContentHandler;

public class BlogConfig {

	private FrequencyLimit limit;
	private HtmlContentHandler beforeHandler;
	private HtmlContentHandler afterHandler;

	public FrequencyLimit getLimit() {
		return limit;
	}

	public HtmlContentHandler getBeforeHandler() {
		return beforeHandler;
	}

	public HtmlContentHandler getAfterHandler() {
		return afterHandler;
	}

	BlogConfig(FrequencyLimit limit, HtmlContentHandler beforeHandler, HtmlContentHandler afterHandler) {
		super();
		this.limit = limit;
		this.beforeHandler = beforeHandler;
		this.afterHandler = afterHandler;
	}

}
