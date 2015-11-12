package me.qyh.dao;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.entity.UserCode;
import me.qyh.entity.UserCodeType;

public interface UserCodeDao extends BaseDao<UserCode, Integer> {

	UserCode selectByUserAndType(@Param(value = "user") User user,
			@Param(value = "type") UserCodeType type);

}
