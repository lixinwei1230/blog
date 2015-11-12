package me.qyh.entity.tag;

public class UserTagCount extends TagModuleCount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserTag tag;

	public UserTag getTag() {
		return tag;
	}

	public void setTag(UserTag tag) {
		this.tag = tag;
	}

	public UserTagCount() {
	}

	public UserTagCount(UserTag tag, TagModule module, int count) {
		super(module, count);
		this.tag = tag;
	}
}
