package me.qyh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import me.qyh.dao.RoleDao;
import me.qyh.dao.SpaceDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.SpaceStatus;
import me.qyh.entity.User;

@Component(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private SpaceDao spaceDao;
	@Autowired
	private MessageSource messageSource;

	@Override
	public UserDetails loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
		User user = (nameOrEmail.indexOf('@') != -1) ? userDao.selectByEmail(nameOrEmail)
				: userDao.selectByName(nameOrEmail);

		if (user == null || !user.getActivate()) {
			throw new UsernameNotFoundException(
					messageSource.getMessage("error.user.notexists", null, LocaleContextHolder.getLocale()));
		}
		user.setRoles(roleDao.selectByUser(user));

		Space space = spaceDao.selectByUser(user);
		if (space != null) {
			if (SpaceStatus.DISABLED.equals(space.getStatus())) {
				user.removeRole(RoleEnum.ROLE_SPACE);
			} else {
				user.setSpace(space);
			}
		}
		return user;
	}

}
