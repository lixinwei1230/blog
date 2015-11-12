package me.qyh.manage.service.impl;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.RoleDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.helper.mail.Mailer;
import me.qyh.helper.mail.MimeMessageHelperHandler;
import me.qyh.manage.service.UserManageService;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserPageParam;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.service.impl.UserServiceImpl;
import me.qyh.utils.Validators;

@Service
public class UserManageServiceImpl extends UserServiceImpl implements UserManageService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private Mailer mailer;
	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional(readOnly = true)
	public Page<User> findUsers(UserPageParam param) {
		List<User> datas = userDao.selectPage(param);
		int count = userDao.selectCount(param);
		return new Page<User>(param, count, datas);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void toggleUserAbled(int id, final TipMessage message) throws LogicException {
		final User user = userDao.selectById(id);
		if (user == null || !user.getActivate()) {
			throw new LogicException("error.user.notexists");
		}
		if (!isOptionalUser(user)) {
			throw new LogicException("error.user.notOptional");
		}
		user.setEnabled(!user.isEnabled());

		userDao.updateUserAbled(user);
		if (Validators.validateEmail(user.getEmail())) {
			mailer.sendEmail(new MimeMessageHelperHandler() {

				@Override
				public void handle(MimeMessageHelper helper) throws MessagingException {
					helper.setTo(user.getEmail());
					helper.setSubject(message.getTitle());
					helper.setText(message.getContent(), true);
				}
			}, true);
		}
	}

	private boolean isOptionalUser(User user) {
		user.setRoles(roleDao.selectByUser(user));
		if (user.hasRole(RoleEnum.ROLE_SUPERVISOR) || user.hasRole(RoleEnum.ROLE_MESSAGER)
				|| user.equals(UserContext.getUser())) {
			return false;
		}
		return true;
	}

}
