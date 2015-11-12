package me.qyh.entity.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.User;
import me.qyh.utils.Validators;

@Component(value = "userValidator")
public class UserValidator implements Validator {

	@Value("${config.validation.user.usernameMaxLength}")
	private int nameMaxLength;
	@Value("${config.validation.user.usernameMinLength}")
	private int nameMinLength;
	@Value("${config.validation.user.emailMaxLength}")
	private int emailMaxLength;

	public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,16}$");

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		User user = (User) o;
		String name = user.getUsername();
		if (Validators.isEmptyOrNull(name, true)) {
			e.rejectValue("username", "validation.username.blank");
			return;
		}
		if (name.length() < nameMinLength || name.length() > nameMaxLength) {
			e.rejectValue("username", "validation.username.length.invalid",
					new Object[] { nameMinLength, nameMaxLength },
					"用户名长度在" + nameMinLength + "和" + nameMaxLength + "之间");
			return;
		}
		String password = user.getPassword();
		if (Validators.isEmptyOrNull(password, true)) {
			e.rejectValue("password", "validation.password.blank");
			return;
		}
		if (!Validators.validate(PASSWORD_PATTERN, password)) {
			e.rejectValue("password", "validation.password.invalid");
			return;
		}
		String email = user.getEmail();
		if (Validators.isEmptyOrNull(email, true)) {
			e.rejectValue("password", "validation.email.blank");
			return;
		}
		if (!Validators.validateEmail(email) || email.length() > emailMaxLength) {
			e.rejectValue("email", "validation.email.invalid");
			return;
		}
	}

}
