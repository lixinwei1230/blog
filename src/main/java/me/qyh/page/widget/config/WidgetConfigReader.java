package me.qyh.page.widget.config;

import org.springframework.validation.Errors;

public interface WidgetConfigReader {

	boolean match(String sign);

	void validate(WidgetConfig o, Errors error);

	Class<? extends WidgetConfig> getConfigClass();

}
