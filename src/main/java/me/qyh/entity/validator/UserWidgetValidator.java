package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.page.widget.UserWidget;
import me.qyh.utils.Validators;

@Component("userWidgetValidator")
public class UserWidgetValidator implements Validator {

	@Value("${config.userWidget.nameMaxLength}")
	private int nameMaxLength;
	@Value("${config.userWidget.htmlMaxLength}")
	private int htmlMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return UserWidget.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		UserWidget uw = (UserWidget) target;
		String name = uw.getName();
		if (Validators.isEmptyOrNull(name, true)) {
			e.rejectValue("name", "validation.userWidget.name.blank");
			return;
		}
		if (name.length() > nameMaxLength) {
			e.rejectValue("name", "validation.userWidget.name.toolong", new Object[] { nameMaxLength },
					"挂件名不能超过" + nameMaxLength + "个字符");
			return;
		}
		String html = uw.getHtml();
		if (Validators.isEmptyOrNull(html, true)) {
			e.rejectValue("html", "validation.userWidget.html.blank");
			return;
		}
		if (html.length() > htmlMaxLength) {
			e.rejectValue("html", "validation.userWidget.html.toolong", new Object[] { htmlMaxLength },
					"挂件内容不能超过" + htmlMaxLength + "个字符");
			return;
		}
	}

}
