package me.qyh.entity.tag;

public class WebTagCount extends TagModuleCount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebTag tag;

	public WebTag getTag() {
		return tag;
	}

	public void setTag(WebTag tag) {
		this.tag = tag;
	}

	public WebTagCount() {
	}

	public WebTagCount(WebTag tag, TagModule module, int count) {
		super(module, count);
		this.tag = tag;
	}

}
