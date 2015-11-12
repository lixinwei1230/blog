package me.qyh.config;

import me.qyh.helper.htmlclean.HtmlContentHandler;

public class BlogConfig {

	private FrequencyLimit limit;
	private HtmlContentHandler clean;

	public FrequencyLimit getLimit() {
		return limit;
	}

	public void setLimit(FrequencyLimit limit) {
		this.limit = limit;
	}

	public HtmlContentHandler getClean() {
		return clean;
	}

	public void setClean(HtmlContentHandler clean) {
		this.clean = clean;
	}
}
