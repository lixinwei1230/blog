package me.qyh.helper.htmlclean;

/**
 * 标签属性
 * 
 * @author mhlx
 *
 */
public class SafeTagAttribute {

	private String name;// 属性名
	private AttributeValueValidator validator;// 属性过滤器

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeValueValidator getValidator() {
		return validator;
	}

	public void setValidator(AttributeValueValidator validator) {
		this.validator = validator;
	}

	public SafeTagAttribute(String name) {
		this.name = name;
	}

}
