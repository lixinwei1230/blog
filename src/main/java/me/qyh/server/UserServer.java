package me.qyh.server;

import java.util.List;

import me.qyh.bean.Scopes;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;

/**
 * 用来提供单纯的用户查询，可以判断被查询用户是否已经被禁用等 不应该使用UserService或者直接使用UserDao来查询用户
 * 
 * @author mhlx
 *
 */
public interface UserServer {

	User getUserById(Integer id) throws LogicException;

	User getUserByNameOrEmail(String nameOrEmail) throws LogicException;

	Scopes userRelationship(User one, User two);

	/**
	 * 得到系统默认的管理员
	 * 
	 * @return
	 */
	List<User> getSupervisors();

	/**
	 * 得到系统默认的信息发送人
	 * 
	 * @return
	 */
	List<User> getMessagers();

	User getUserBySpace(Space space) throws LogicException;

}
