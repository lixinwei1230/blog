package me.qyh.config;

import me.qyh.helper.html.HtmlContentHandler;

public class BlogCommentConfig {

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

	BlogCommentConfig(FrequencyLimit limit, HtmlContentHandler beforeHandler, HtmlContentHandler afterHandler) {
		super();
		this.limit = limit;
		this.beforeHandler = beforeHandler;
		this.afterHandler = afterHandler;
	}
}
