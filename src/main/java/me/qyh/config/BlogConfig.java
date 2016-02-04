package me.qyh.config;

import me.qyh.helper.html.HtmlContentHandler;

public class BlogConfig {

	private FrequencyLimit limit;
	private HtmlContentHandler beforeHandler;
	private HtmlContentHandler afterHandler;

	public FrequencyLimit getLimit() {
		return limit;
	}

	public void setLimit(FrequencyLimit limit) {
		this.limit = limit;
	}

	public HtmlContentHandler getBeforeHandler() {
		return beforeHandler;
	}

	public void setBeforeHandler(HtmlContentHandler beforeHandler) {
		this.beforeHandler = beforeHandler;
	}

	public HtmlContentHandler getAfterHandler() {
		return afterHandler;
	}

	public void setAfterHandler(HtmlContentHandler afterHandler) {
		this.afterHandler = afterHandler;
	}

}
