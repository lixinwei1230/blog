package me.qyh.page.widget.config.support;

import me.qyh.page.widget.config.WidgetConfigReader;

public abstract class AbstractWidgetConfigReader implements WidgetConfigReader {

	private String sign;

	public AbstractWidgetConfigReader(String sign) {
		this.sign = sign;
	}

	@Override
	public boolean match(String sign) {
		return sign.equalsIgnoreCase(this.sign);
	}

}
