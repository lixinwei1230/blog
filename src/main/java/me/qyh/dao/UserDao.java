package me.qyh.dao;

import java.util.List;

import me.qyh.entity.Role;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.pageparam.UserPageParam;

import org.apache.ibatis.annotations.Param;

public interface UserDao extends BaseDao<User, Integer> {

	User selectByName(String name);

	User selectByEmail(String email);

	User selectBySpace(Space space);

	int insertUserRole(@Param(value = "user") User user, @Param(value = "role") Role role);

	User selectById(Integer id);

	List<User> selectByRole(RoleEnum role);

	List<User> selectPage(UserPageParam param);

	int selectCount(UserPageParam param);

	int updateUserAbled(User user);

	/**
	 * 更新用户的用户名、邮箱等固定项
	 * 
	 * @param user
	 * @return
	 */
	int updateFixedTerm(User user);

	void deleteUserRole(@Param(value = "user") User user, @Param(value = "role") Role role);

}
