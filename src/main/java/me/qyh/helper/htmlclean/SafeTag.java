package me.qyh.helper.htmlclean;

import java.util.HashSet;
import java.util.Set;

/**
 * 安全标签
 * 
 * @author mhlx
 *
 */
public class SafeTag {

	private String name;// 标签名
	private Set<SafeTagAttribute> atts = new HashSet<SafeTagAttribute>();// 标签属性

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<SafeTagAttribute> getAtts() {
		return atts;
	}

	public void setAtts(Set<SafeTagAttribute> atts) {
		this.atts = atts;
	}

	public SafeTag(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return "SafeTag [name=" + name + ", atts=" + atts + "]";
	}

}
