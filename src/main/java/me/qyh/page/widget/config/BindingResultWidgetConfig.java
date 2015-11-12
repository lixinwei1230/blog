package me.qyh.page.widget.config;

import org.springframework.validation.BindingResult;

public class BindingResultWidgetConfig {

	private BindingResult result;
	private WidgetConfig config;

	public BindingResultWidgetConfig(BindingResult result,
			WidgetConfig config) {
		super();
		this.result = result;
		this.config = config;
	}

	public BindingResult getResult() {
		return result;
	}

	public WidgetConfig getConfig() {
		return config;
	}

	public boolean hasErrors() {
		return result.hasErrors();
	}

}
