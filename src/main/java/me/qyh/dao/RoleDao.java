package me.qyh.dao;

import java.util.List;

import me.qyh.entity.Role;
import me.qyh.entity.Role.RoleEnum;
import me.qyh.entity.User;

public interface RoleDao extends BaseDao<Role, Integer> {

	Role selectByRoleName(RoleEnum role);

	List<Role> selectByUser(User user);

}
