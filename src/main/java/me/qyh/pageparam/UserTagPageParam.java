package me.qyh.pageparam;

import me.qyh.entity.User;
import me.qyh.web.InvalidParamException;

public class UserTagPageParam extends WebTagPageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private boolean owner;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if (user == null) {
			throw new InvalidParamException();
		}
	}

}
