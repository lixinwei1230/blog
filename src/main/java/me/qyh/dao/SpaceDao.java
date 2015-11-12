package me.qyh.dao;

import me.qyh.entity.Space;
import me.qyh.entity.User;

public interface SpaceDao extends BaseDao<Space, String> {

	Space selectByUser(User user);

	int updateSpaceStatus(Space space);

}
