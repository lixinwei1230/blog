package me.qyh.page.widget.config.support;

import org.springframework.validation.Errors;

import me.qyh.page.widget.config.WidgetConfig;

public class BlogWidgetConfigReader extends AbstractWidgetConfigReader {

	public BlogWidgetConfigReader(String sign) {
		super(sign);
	}

	@Override
	public Class<? extends WidgetConfig> getConfigClass() {
		return BlogWidgetConfig.class;
	}

	@Override
	public void validate(WidgetConfig o, Errors e) {
		BlogWidgetConfig config = (BlogWidgetConfig) o;
		if (config.getMode() == null) {
			e.rejectValue("mode", "validation.widget.config.blogWidgetConfig.mode.blank");
			return;
		}
	}

}
