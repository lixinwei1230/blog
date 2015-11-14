package me.qyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.bean.Scopes;
import me.qyh.dao.SpaceDao;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.exception.SpaceDisabledException;
import me.qyh.server.SpaceServer;

@Component
public class SpaceServerImpl implements SpaceServer {

	@Autowired
	private SpaceDao spaceDao;

	@Override
	@Transactional(readOnly = true)
	public Space getSpaceById(String id) throws LogicException, SpaceDisabledException {
		Space space = spaceDao.selectById(id);
		if (space == null) {
			throw new LogicException("error.space.notexists");
		}

		validSpace(space);
		return space;
	}

	@Override
	@Transactional(readOnly = true)
	public Space getSpaceByUser(User user) throws LogicException, SpaceDisabledException {
		Space space = spaceDao.selectByUser(user);
		if (space == null) {
			throw new LogicException("error.space.notexists");
		}

		validSpace(space);
		return space;
	}

	@Override
	@Transactional(readOnly = true)
	public Scopes getScopes(User visitor, Space toVisit) {
		if (visitor != null && toVisit != null) {
			// 这里也许需要按照用户设置的访问权限表
			try {
				Space db = getSpaceById(toVisit.getId());
				if (visitor.equals(db.getUser())) {
					return Scopes.ALL;
				}
			} catch (LogicException e) {

			}
		}
		return Scopes.PUBLIC;
	}

	private void validSpace(Space space) throws SpaceDisabledException {
		switch (space.getStatus()) {
		case CLOSED:
			throw new SpaceDisabledException(space, "error.space.closed");
		case DISABLED:
			throw new SpaceDisabledException(space, "error.space.disabled");
		default:
			break;
		}
	}

}
