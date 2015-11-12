package me.qyh.page.widget.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.exception.SystemException;

public class WidgetConfigValidator implements Validator {

	private List<WidgetConfigReader> readers = new ArrayList<WidgetConfigReader>();

	@Override
	public boolean supports(Class<?> clazz) {
		return WidgetConfig.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		WidgetConfig config = (WidgetConfig) target;
		if (config.getWidget() == null) {
			e.rejectValue("mode", "validation.widget.config.widget.blank");
			return;
		}
		WidgetConfigReader reader = getReader(config);
		reader.validate(config, e);
	}

	private WidgetConfigReader getReader(WidgetConfig o) {
		for (WidgetConfigReader reader : readers) {
			if (reader.getConfigClass().equals(o.getClass())) {
				return reader;
			}
		}
		throw new SystemException("无法找到WidgetConfig验证器");
	}

	public List<WidgetConfigReader> getReaders() {
		return readers;
	}

	public void setReaders(List<WidgetConfigReader> readers) {
		this.readers = readers;
	}

}
