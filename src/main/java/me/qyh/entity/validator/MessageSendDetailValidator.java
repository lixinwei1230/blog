package me.qyh.entity.validator;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.message.MessageDetail;
import me.qyh.entity.message.MessageSendDetail;
import me.qyh.utils.Validators;

@Component(value = "messageSendDetailValidator")
public class MessageSendDetailValidator implements Validator {

	@Value("${config.validation.messageSend.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.messageSend.contentMaxLength}")
	private int contentMaxLength;
	@Value("${config.validation.messageSend.receiversMaxSize}")
	private int receiversMaxSize;

	@Override
	public boolean supports(Class<?> clazz) {
		return MessageSendDetail.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		MessageSendDetail send = (MessageSendDetail) o;
		MessageDetail detail = send.getDetail();
		if (detail == null) {
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

		Set<String> receivers = send.getReceivers();
		if (receivers.isEmpty()) {
			e.rejectValue("receivers", "validation.messageSendDetail.receivers.empty");
			return;
		}
		if (receivers.size() > receiversMaxSize) {
			e.rejectValue("receivers", "validation.messageSendDetail.receivers.oversize",
					new Object[] { receiversMaxSize }, "接收人不能超过" + receiversMaxSize + "个");
		}
	}

}
