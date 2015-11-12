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

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if (module == null) {
			throw new InvalidParamException();
		}
	}

}
