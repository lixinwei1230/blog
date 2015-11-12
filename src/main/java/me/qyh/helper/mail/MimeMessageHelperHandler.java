package me.qyh.helper.mail;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;

public interface MimeMessageHelperHandler {

	void handle(MimeMessageHelper helper) throws MessagingException;

}
