package me.qyh.helper.htmlclean;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全标签
 * 
 * @author mhlx
 *
 */
public class SafeTag {

	private String name;// 标签名
	private List<SafeTagAttribute> atts = new ArrayList<SafeTagAttribute>();// 标签属性

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SafeTagAttribute> getAtts() {
		return atts;
	}

	public void setAtts(List<SafeTagAttribute> atts) {
		this.atts = atts;
	}

	public SafeTag(String name) {
		super();
		this.name = name;
	}

}
