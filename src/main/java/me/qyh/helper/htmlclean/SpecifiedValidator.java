package me.qyh.helper.htmlclean;

import java.util.HashSet;
import java.util.Set;

public class SpecifiedValidator {

	private String tagName;
	private Set<String> attNames = new HashSet<String>();
	private AttributeValueValidator validator;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Set<String> getAttNames() {
		return attNames;
	}

	public void setAttNames(Set<String> attNames) {
		this.attNames = attNames;
	}

	public AttributeValueValidator getValidator() {
		return validator;
	}

	public void setValidator(AttributeValueValidator validator) {
		this.validator = validator;
	}

	public SpecifiedValidator(String tagName, Set<String> attNames) {
		this.tagName = tagName;
		this.attNames = attNames;
	}

	public SpecifiedValidator(Set<String> attNames) {
		this.attNames = attNames;
	}

}
