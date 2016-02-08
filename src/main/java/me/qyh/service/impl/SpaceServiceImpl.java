package me.qyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.RoleDao;
import me.qyh.dao.SpaceDao;
import me.qyh.dao.UserDao;
import me.qyh.entity.Role;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.exception.LogicException;
import me.qyh.server.UserServer;
import me.qyh.service.SpaceService;

@Service("spaceService")
public class SpaceServiceImpl extends BaseServiceImpl implements SpaceService {

	@Autowired
	private SpaceDao spaceDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private SpaceOpenSuccessHandler spaceOpenSuccessHandler;
	@Autowired
	private UserServer userServer;
	private SpaceNameChecker spaceNameChecker = new NoChecker();

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void openSpace(Space space) throws LogicException {
		// 检查用户，如果用户被禁用，无法开通
		userServer.getUserById(space.getUser().getId());
		spaceNameChecker.check(space.getId());
		Space db = spaceDao.selectById(space.getId());
		if (db != null) {
			throw new LogicException("error.space.exists");
		}
		if (spaceDao.selectByUser(space.getUser()) != null) {
			throw new LogicException("error.space.opened");
		}
		spaceDao.insert(space);

		Role dbRole = roleDao.selectByRoleName(RoleEnum.ROLE_SPACE);
		userDao.insertUserRole(space.getUser(), dbRole);

		spaceOpenSuccessHandler.handler(space);
	}

	public void setSpaceNameChecker(SpaceNameChecker spaceNameChecker) {
		this.spaceNameChecker = spaceNameChecker;
	}
	
	private final class NoChecker implements SpaceNameChecker{

		@Override
		public void check(String spaceName) throws LogicException {
			
		}
	}
}
