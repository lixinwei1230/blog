package me.qyh.pageparam;

import me.qyh.web.InvalidParamException;

public class TagPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
	}

}
