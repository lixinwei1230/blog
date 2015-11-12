package me.qyh.helper.htmlclean;

import me.qyh.utils.Validators;

public class SrcValidator implements AttributeValueValidator {

	private boolean allowRelativePath = true;

	@Override
	public boolean allow(String value) {
		return (!Validators.validateUrl(value)
				|| (allowRelativePath && !Validators
						.validateUrl("http://www.example.com".concat(value))));
	}

	public void setAllowRelativePath(boolean allowRelativePath) {
		this.allowRelativePath = allowRelativePath;
	}
}
