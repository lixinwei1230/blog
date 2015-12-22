package me.qyh.entity.validator;

import java.util.Set;

import me.qyh.entity.message.MessageSendDetail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component(value = "messageSendDetailValidator")
public class MessageSendDetailValidator extends MessageSendValidator {

	@Value("${config.validation.messageSend.receiversMaxSize}")
	private int receiversMaxSize;

	@Override
	public boolean supports(Class<?> clazz) {
		return MessageSendDetail.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		super.validate(o, e);
		
		MessageSendDetail send = (MessageSendDetail)o;
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
