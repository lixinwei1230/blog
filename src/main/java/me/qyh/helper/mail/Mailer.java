package me.qyh.helper.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import me.qyh.exception.SystemException;

/**
 * 发送邮件
 * 
 * @author mhlx
 *
 */
@Component
public class Mailer {

	private static final String CHARSET = "utf-8";

	@Autowired
	private JavaMailSender mailSender;
	private String charset = CHARSET;

	/**
	 * 不需要设置from
	 * 
	 * @param handler
	 * @param multi
	 */
	public void sendEmail(final MimeMessageHelperHandler handler,
			final boolean multi) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				handler.handle(
						new MimeMessageHelper(mimeMessage, multi, charset));
				mimeMessage.setFrom();
			}
		};
		try {
			mailSender.send(preparator);
		} catch (MailException e) {
			throw new SystemException(e);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
