package me.qyh.pageparam;

import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.User;
import me.qyh.web.InvalidParamException;

public class CommentPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CommentScope scope;
	private Comment parent;
	private User user;

	public CommentScope getScope() {
		return scope;
	}

	public void setScope(CommentScope scope) {
		this.scope = scope;
	}

	public Comment getParent() {
		return parent;
	}

	public void setParent(Comment parent) {
		this.parent = parent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void validate() throws InvalidParamException {
		super.validate();
		if (scope == null || scope.getScopeId() == null || scope.getScope() == null) {
			throw new InvalidParamException();
		}
	}
}
