package me.qyh.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.bean.Scopes;
import me.qyh.dao.RoleDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.MyFile;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.server.UserServer;

@Component
public class UserServerImpl implements UserServer {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional(readOnly = true)
	public User getUserById(Integer id) throws LogicException {
		User user = userDao.selectById(id);
		validUser(user);
		user.setRoles(roleDao.selectByUser(user));
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserByNameOrEmail(String nameOrEmail) throws LogicException {
		User user = (nameOrEmail.indexOf('@') != -1)
				? userDao.selectByEmail(nameOrEmail)
				: userDao.selectByName(nameOrEmail);
		validUser(user);
		user.setRoles(roleDao.selectByUser(user));
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findUserBySpaces(Set<String> spaces) {
		return userDao.selectBySpaces(spaces);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findUserByIds(Set<Integer> ids) {
		return userDao.selectByIds(ids);
	}

	@Override
	@Transactional(readOnly = true)
	public MyFile getAvatar(Integer id) {
		return userDao.selectAvatar(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Scopes userRelationship(User one, User two) {
		if (one == null || !one.equals(two)) {
			return Scopes.PUBLIC;
		}
		return Scopes.ALL;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getSupervisors() {
		return userDao.selectByRole(RoleEnum.ROLE_SUPERVISOR);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getMessagers() {
		return userDao.selectByRole(RoleEnum.ROLE_MESSAGER);
	}

	@Override
	@Transactional(readOnly = true)
	public User getUserBySpace(Space space) throws LogicException {
		User user = userDao.selectBySpace(space);
		validUser(user);
		user.setRoles(roleDao.selectByUser(user));
		return user;
	}
	
	private void validUser(User user) throws LogicException {
		if (user == null || !user.getActivate()) {
			throw new DataNotFoundException("error.user.notexists");
		}
		if (!user.isAccountNonExpired()) {
			throw new LogicException("error.account.expired");
		}
		if (!user.isAccountNonLocked()) {
			throw new LogicException("error.account.locked");
		}
		if (!user.isEnabled()) {
			throw new LogicException("error.account.disabled");
		}
	}


}
