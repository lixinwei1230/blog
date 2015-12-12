package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.entity.tag.Tag;
import me.qyh.entity.tag.UserTag;
import me.qyh.pageparam.UserTagPageParam;

public interface UserTagDao extends BaseDao<UserTag, Integer> {

	List<UserTag> selectPage(UserTagPageParam param);

	UserTag selectByTag(@Param("tag") Tag tag,@Param("user") User user);

	int selectCount(UserTagPageParam param);

}
