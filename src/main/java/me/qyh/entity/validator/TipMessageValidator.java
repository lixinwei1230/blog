package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.server.TipMessage;
import me.qyh.utils.Validators;

@Component("tipMessageValidator")
public class TipMessageValidator implements Validator {

	@Value("${config.validation.messageSend.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.messageSend.contentMaxLength}")
	private int contentMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return TipMessage.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		TipMessage message = (TipMessage) o;
		String title = message.getTitle();

		if (Validators.isEmptyOrNull(title, true)) {
			e.rejectValue("title", "validation.messageSendDetail.detail.title.empty");
			return;
		}

		if (title.length() > titleMaxLength) {
			e.rejectValue("title", "validation.messageSendDetail.detail.title.toolong", new Object[] { titleMaxLength },
					"信息标题长度不 能超过" + titleMaxLength + "个字符");
			return;
		}

		String content = message.getContent();
		if (Validators.isEmptyOrNull(content, true)) {
			e.rejectValue("content", "validation.messageSendDetail.detail.content.empty");
			return;
		}
		if (content.length() > contentMaxLength) {
			e.rejectValue("content", "validation.messageSendDetail.detail.content.toolong",
					new Object[] { contentMaxLength }, "信息内容长度不 能超过" + contentMaxLength + "个字符");
		}
	}

}
