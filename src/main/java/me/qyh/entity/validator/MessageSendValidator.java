package me.qyh.entity.validator;

import me.qyh.entity.message.MessageDetail;
import me.qyh.entity.message.MessageSend;
import me.qyh.utils.Validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(value = "messageSendValidator")
public class MessageSendValidator implements Validator {

	@Value("${config.validation.messageSend.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.messageSend.contentMaxLength}")
	private int contentMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return MessageSend.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		MessageSend send = (MessageSend) o;
		MessageDetail detail = send.getDetail();
		if (detail == null || detail.hasId()) {
			e.rejectValue("detail", "validation.messageSendDetail.detail.empty");
			return;
		}
		String title = detail.getTitle();

		if (Validators.isEmptyOrNull(title, true)) {
			e.rejectValue("detail", "validation.messageSendDetail.detail.title.empty");
			return;
		}

		if (title.length() > titleMaxLength) {
			e.rejectValue("detail", "validation.messageSendDetail.detail.title.toolong",
					new Object[] { titleMaxLength }, "信息标题长度不 能超过" + titleMaxLength + "个字符");
			return;
		}

		String content = detail.getContent();
		if (Validators.isEmptyOrNull(content, true)) {
			e.rejectValue("detail", "validation.messageSendDetail.detail.content.empty");
			return;
		}
		if (content.length() > contentMaxLength) {
			e.rejectValue("detail", "validation.messageSendDetail.detail.content.toolong",
					new Object[] { contentMaxLength }, "信息内容长度不 能超过" + contentMaxLength + "个字符");
		}
	}
}
