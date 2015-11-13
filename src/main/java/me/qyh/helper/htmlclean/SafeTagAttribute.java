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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SafeTagAttribute other = (SafeTagAttribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SafeTagAttribute [name=" + name + "]";
	}
}
