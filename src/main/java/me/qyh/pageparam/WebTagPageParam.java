package me.qyh.pageparam;

import me.qyh.entity.tag.TagModule;
import me.qyh.web.InvalidParamException;

public class WebTagPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TagModule module;
	private String name;
	private boolean loadCount;

	public TagModule getModule() {
		return module;
	}

	public void setModule(TagModule module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoadCount() {
		return loadCount;
	}

	public void setLoadCount(boolean loadCount) {
		this.loadCount = loadCount;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if (module == null) {
			throw new InvalidParamException();
		}
	}

}
