package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.page.LocationWidget;
import me.qyh.page.Page;
import me.qyh.page.widget.Widget;

@Component("locationWidgetValidator")
public class LocationWidgetValidator implements Validator {

	private static final int maxWidth = 100;

	@Value("${config.locationWidget.maxR}")
	private int maxR;
	@Value("${config.locationWidget.maxY}")
	private int maxY;
	@Value("${config.locationWidget.maxX}")
	private int maxX;
	@Value("${config.locationWidget.minWidth}")
	private int minWidth;

	@Override
	public boolean supports(Class<?> clazz) {
		return LocationWidget.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		LocationWidget lw = (LocationWidget) target;
		Page page = lw.getPage();
		if (page == null || !page.hasId()) {
			e.rejectValue("page", "validation.widget.page.blank");
			return;
		}

		Widget widget = lw.getWidget();
		if (widget == null || !widget.hasId()) {
			e.rejectValue("widget", "validation.widget.blank");
			return;
		}
		if (widget.getType() == null) {
			e.rejectValue("type", "validation.widget.type.blank");
			return;
		}
		if (lw.getWidth() < minWidth) {
			e.rejectValue("width", "validation.widget.width.toosmall", new Object[] { minWidth },
					"挂件宽度不能小于" + minWidth);
			return;
		}
		if (lw.getWidth() > maxWidth) {
			e.rejectValue("width", "validation.widget.width.toolong", new Object[] { maxWidth }, "挂件宽度不能超过" + maxWidth);
			return;
		}
		if (lw.getR() < 0) {
			e.rejectValue("r", "validation.widget.r.invalid");
			return;
		}
		if (lw.getR() > maxR) {
			e.rejectValue("r", "validation.widget.r.toolong", new Object[] { maxR }, "挂件所在行数不能超过" + maxR);
			return;
		}
		if (lw.getX() < 0) {
			e.rejectValue("x", "validation.widget.x.invalid");
			return;
		}
		if (lw.getX() > maxX) {
			e.rejectValue("x", "validation.widget.x.toolong", new Object[] { maxX }, "挂件所在列数不能超过" + maxX);
			return;
		}
		if (lw.getY() < 0) {
			e.rejectValue("y", "validation.widget.y.invalid");
			return;
		}
		if (lw.getY() > maxY) {
			e.rejectValue("y", "validation.widget.y.toolong", new Object[] { maxY }, "挂件所在个数不能超过" + maxY);
			return;
		}

	}

}
