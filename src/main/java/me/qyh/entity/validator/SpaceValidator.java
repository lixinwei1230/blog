package me.qyh.entity.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.Space;
import me.qyh.utils.Validators;

@Component
public class SpaceValidator implements Validator {

	public static final String ID_PATTERN = "^[a-z]{3,11}$";

	@Override
	public boolean supports(Class<?> clazz) {
		return Space.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		Space space = (Space) target;
		String id = space.getId();
		if (Validators.isEmptyOrNull(id, true)) {
			e.rejectValue("id", "validation.space.id.blank");
			return;
		}
		if (!Validators.validate(Pattern.compile(ID_PATTERN), id)) {
			e.rejectValue("id", "validation.space.id.invalid");
			return;
		}
	}

}
