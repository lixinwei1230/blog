package me.qyh.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.RoleDao;
import me.qyh.dao.SpaceDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.Role.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.Space.SpaceStatus;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.SpaceManageService;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.SpaceServiceImpl;

@Service
public class SpaceManageServiceImpl extends SpaceServiceImpl implements SpaceManageService {

	@Autowired
	private SpaceDao spaceDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private TipServer tipServer;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void toggleSpaceAbled(String id, TipMessage message) throws LogicException {
		Space space = spaceDao.selectById(id);
		if (space == null) {
			throw new LogicException("error.space.notexists");
		}
		if (!isOptionalSpace(space)) {
			throw new LogicException("error.space.notOptional");
		}
		switch (space.getStatus()) {
		case CLOSED:
			return;
		case DISABLED:
			space.setStatus(SpaceStatus.NORMAL);
			break;
		case NORMAL:
			space.setStatus(SpaceStatus.DISABLED);
			break;
		}
		spaceDao.updateSpaceStatus(space);

		message.setReceiver(space.getUser());
		message.setSender(UserContext.getUser());

		tipServer.sendTip(message);
	}

	private boolean isOptionalSpace(Space space) {
		User user = userDao.selectById(space.getUser().getId());
		user.setRoles(roleDao.selectByUser(user));
		if (user.hasRole(RoleEnum.ROLE_SUPERVISOR) || user.hasRole(RoleEnum.ROLE_MESSAGER)
				|| user.equals(UserContext.getUser())) {
			return false;
		}
		return true;
	}

}
