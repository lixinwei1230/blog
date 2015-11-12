package me.qyh.entity.tag;

import me.qyh.entity.Id;

public class TagModuleCount extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TagModule module;
	private int count;

	public TagModule getModule() {
		return module;
	}

	public void setModule(TagModule module) {
		this.module = module;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public TagModuleCount() {
		super();
	}

	public TagModuleCount(TagModule module, int count) {
		this.module = module;
		this.count = count;
	}

}
