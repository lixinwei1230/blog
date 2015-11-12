package me.qyh.page.widget.config.support;

import org.springframework.validation.Errors;

import me.qyh.page.widget.config.WidgetConfig;

public class DefaultWidgetConfigReader extends AbstractWidgetConfigReader {

	@Override
	public void validate(WidgetConfig o, Errors error) {

	}

	@Override
	public Class<? extends WidgetConfig> getConfigClass() {
		return WidgetConfig.class;
	}

	public DefaultWidgetConfigReader(String sign) {
		super(sign);
	}

}
