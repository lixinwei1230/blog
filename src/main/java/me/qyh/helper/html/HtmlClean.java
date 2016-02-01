package me.qyh.helper.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import me.qyh.exception.SystemException;
import me.qyh.helper.refresh.Refresh;

/**
 * html片段清理
 * 
 * @author mhlx
 *
 */
public class HtmlClean implements HtmlContentHandler, InitializingBean, Refresh, ResourceLoaderAware {

	/**
	 * 允许的标签
	 * 
	 * @see SafeTag
	 */
	private List<SafeTag> tags = new ArrayList<SafeTag>();
	private String html;
	private String location;
	private List<SpecifiedValidator> specifiedValidators = new ArrayList<SpecifiedValidator>();
	private Set<Tag> ts = new HashSet<Tag>();
	private ResourceLoader resourceLoader;

	@Override
	public String handle(String htmlContent) {
		return Jsoup.clean(htmlContent, new ThisWhitelist());
	}

	@Override
	public void refresh() throws Exception {
		synchronized (this) {
			initSafeTag();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initSafeTag();
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setSpecifiedValidators(List<SpecifiedValidator> specifiedValidators) {
		this.specifiedValidators = specifiedValidators;
	}

	private void initSafeTag() {
		if(!ts.isEmpty()){
			ts.clear();
		}
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
		List<SafeTag> tags = new ArrayList<SafeTag>(this.ts.size());
		for (Tag tag : this.ts) {
			SafeTag _tag = new SafeTag(tag.name);
			Set<SafeTagAttribute> stAtts = new HashSet<SafeTagAttribute>();
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
		this.tags = tags;
	}

	private class ThisWhitelist extends Whitelist {

		@Override
		protected boolean isSafeAttribute(String tag, Element e, Attribute att) {
			if (!Jsoup.isValid(att.getValue(), Whitelist.none())) {
				return false;
			}

			for (SafeTag st : tags) {
				if (tag.equals(st.getName())) {
					Set<SafeTagAttribute> allowAttrs = st.getAtts();
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
		Set<String> classes = parseValue(value);
		for (String clazz : classes) {
			if (!validator.allow(clazz)) {
				return false;
			}
		}

		return true;
	}

	private void addTag(Tag tag) {
		if (ts.contains(tag)) {
			for (Tag _tag : ts) {
				if (_tag.equals(tag)) {
					for (TagAttribute ta : tag.atts) {
						_tag.addAttrbute(ta);
					}
				}
			}
		} else {
			ts.add(tag);
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

	private String getHtml() {
		if (html != null) {
			return html;
		}
		Resource resource = resourceLoader.getResource(location);
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
}
