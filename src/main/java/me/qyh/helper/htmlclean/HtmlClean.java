package me.qyh.helper.htmlclean;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * html片段清理
 * 
 * @author mhlx
 *
 */
public class HtmlClean implements HtmlContentHandler {

	/**
	 * 允许的标签
	 * 
	 * @see SafeTag
	 */
	private List<SafeTag> tags = new ArrayList<SafeTag>();

	private class ThisWhitelist extends Whitelist {

		@Override
		protected boolean isSafeAttribute(String tag, Element e, Attribute att) {
			if (!Jsoup.isValid(att.getValue(), Whitelist.none())) {
				return false;
			}

			for (SafeTag st : tags) {
				if (tag.equals(st.getName())) {
					List<SafeTagAttribute> allowAttrs = st.getAtts();
					if (allowAttrs.isEmpty() && e.attributes().size() > 0) {
						return false;
					}
					for (SafeTagAttribute allowAtt : allowAttrs) {
						String attName = allowAtt.getName();
						if (attName.equals(att.getKey())) {
							AttributeValueValidator validator = allowAtt.getValidator();
							return validator == null ? true
									: "class".equalsIgnoreCase(att.getKey()) ? validClasses(validator, att.getValue())
											: validator.allow(att.getValue());
						}
					}
					return false;
				}
			}
			return false;
		}

		@Override
		protected boolean isSafeTag(String tag) {
			for (SafeTag st : tags) {
				if (tag.equals(st.getName())) {
					return true;
				}
			}
			return false;
		}
	}

	private boolean validClasses(AttributeValueValidator validator, String value) {
		List<String> classes = parseValue(value);
		for (String clazz : classes) {
			if (!validator.allow(clazz)) {
				return false;
			}
		}

		return true;
	}

	private List<String> parseValue(String value) {
		String[] values = value.split(" ");
		List<String> _values = new ArrayList<String>();
		for (String _value : values) {
			if (!_value.isEmpty()) {
				_values.add(_value);
			}
		}
		return _values;
	}

	public void setTags(List<SafeTag> tags) {
		this.tags = tags;
	}

	@Override
	public String handle(String htmlContent) {
		return Jsoup.clean(htmlContent, new ThisWhitelist());
	}

}
