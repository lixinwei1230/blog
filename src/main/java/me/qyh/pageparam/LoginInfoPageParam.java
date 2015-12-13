package me.qyh.pageparam;

import me.qyh.entity.User;
import me.qyh.web.InvalidParamException;

public class LoginInfoPageParam extends PageParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if(user == null){
			throw new InvalidParamException();
		}
	}
}
