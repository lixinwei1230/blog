package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.tag.TagModule;
import me.qyh.entity.tag.UserTag;
import me.qyh.entity.tag.UserTagCount;

public interface UserTagCountDao extends BaseDao<UserTagCount, Integer> {

	Integer updateCount(@Param("ut") UserTag ut, @Param("module") TagModule module, @Param("count") int count);

	List<UserTagCount> selectByTag(UserTag tag);
}
