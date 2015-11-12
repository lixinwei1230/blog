package me.qyh.helper.htmlclean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.qyh.exception.SystemException;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

public class HtmlCleanFactoryBean implements FactoryBean<HtmlClean>, ApplicationContextAware {

	private String html;
	private String location;
	private List<SpecifiedValidator> specifiedValidators = new ArrayList<SpecifiedValidator>();
	private Set<Tag> tags = new HashSet<Tag>();

	@Override
	public HtmlClean getObject() throws Exception {
		Document doc = Jsoup.parseBodyFragment(getHtml());
		Elements eles = doc.body().getAllElements();
		if (!eles.isEmpty()) {
			for (Element element : eles) {
				String nodeName = element.nodeName();
				if ("body".equalsIgnoreCase(nodeName)) {
					continue;
				}
				Tag tag = new Tag();
				tag.name = element.nodeName();
				for (Attribute att : element.attributes()) {
					String key = att.getKey();
					if ("class".equalsIgnoreCase(key)) {
						tag.addAttrbute(new TagAttribute("class", element.classNames()));
					} else {
						String value = att.getValue();
						tag.addAttrbute("".equals(value) ? new TagAttribute(key)
								: new TagAttribute(key, parseValue(att.getValue())));
					}
				}
				addTag(tag);
			}
		}
		List<SafeTag> tags = new ArrayList<SafeTag>(this.tags.size());
		for (Tag tag : this.tags) {
			SafeTag _tag = new SafeTag(tag.name);
			List<SafeTagAttribute> stAtts = new ArrayList<SafeTagAttribute>();
			for (TagAttribute att : tag.atts) {
				SafeTagAttribute stAtt = new SafeTagAttribute(att.name);
				AttributeValueValidator validator = seekeValidatorFromSpeicifiedValidators(tag.name, att.name);
				if (validator != null) {
					stAtt.setValidator(validator);
				} else if (!att.allows.isEmpty()) {
					stAtt.setValidator(new StringsValidator(att.allows));
				}
				stAtts.add(stAtt);
			}
			_tag.setAtts(stAtts);
			tags.add(_tag);
		}
		HtmlClean clean = new HtmlClean();
		clean.setTags(tags);
		return clean;
	}

	@Override
	public Class<?> getObjectType() {
		return HtmlClean.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHtml() {
		if (html != null) {
			return html;
		}
		Resource resource = ctx.getResource(location);
		InputStream is = null;
		try {
			is = resource.getInputStream();
			return IOUtils.toString(is);
		} catch (IOException e) {
			throw new SystemException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public void setSpecifiedValidators(List<SpecifiedValidator> specifiedValidators) {
		this.specifiedValidators = specifiedValidators;
	}

	private void addTag(Tag tag) {
		if (tags.contains(tag)) {
			for (Tag _tag : tags) {
				if (_tag.equals(tag)) {
					for (TagAttribute ta : tag.atts) {
						_tag.addAttrbute(ta);
					}
				}
			}
		} else {
			tags.add(tag);
		}
	}

	private Set<String> parseValue(String value) {
		String[] values = value.split(" ");
		Set<String> _values = new HashSet<String>();
		for (String _value : values) {
			if (!_value.isEmpty()) {
				_values.add(_value);
			}
		}
		return _values;
	}

	private AttributeValueValidator seekeValidatorFromSpeicifiedValidators(String tagName, String attName) {
		if (!specifiedValidators.isEmpty()) {
			for (SpecifiedValidator sv : specifiedValidators) {
				String _tagName = sv.getTagName();
				Set<String> _attNames = sv.getAttNames();
				if (_tagName != null) {
					if (tagName.equalsIgnoreCase(_tagName) && _attNames.contains(attName)) {
						return sv.getValidator();
					}
				} else {
					if (_attNames.contains(attName)) {
						return sv.getValidator();
					}
				}
			}
		}
		return null;
	}

	private class TagAttribute {

		private String name;

		private Set<String> allows = new HashSet<String>();

		public TagAttribute addAllows(Set<String> allows) {
			this.allows.addAll(allows);
			return this;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			TagAttribute that = (TagAttribute) o;

			if (name != null ? !name.equals(that.name) : that.name != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}

		public TagAttribute(String name, Set<String> allows) {
			this.name = name;
			this.allows = allows;
		}

		public TagAttribute(String name) {
			this.name = name;
		}
	}

	private class Tag {

		private String name;

		private Set<TagAttribute> atts = new HashSet<TagAttribute>();

		public Tag addAttrbute(TagAttribute att) {
			if (atts.contains(att)) {
				for (TagAttribute _att : atts) {
					if (_att.equals(att)) {
						_att.addAllows(att.allows);
					}
				}
			} else {
				atts.add(att);
			}
			return this;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Tag tag = (Tag) o;

			if (name != null ? !name.equals(tag.name) : tag.name != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}
	}

	private class StringsValidator implements AttributeValueValidator {

		private Set<String> allows = new HashSet<String>();

		@Override
		public boolean allow(String value) {
			for (String allowValue : allows) {
				if (allowValue.equals(value)) {
					return true;
				}
			}
			return false;
		}

		public StringsValidator(Set<String> allows) {
			this.allows = allows;
		}
	}

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

}
