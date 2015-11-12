package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.entity.tag.UserTag;
import me.qyh.pageparam.UserTagPageParam;

public interface UserTagDao extends BaseDao<UserTag, Integer> {

	UserTag selectByName(@Param("name") String name, @Param("user") User user);

	int selectCount(UserTagPageParam param);

	List<UserTag> selectPage(UserTagPageParam param);

}
