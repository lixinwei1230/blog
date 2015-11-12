package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.tag.TagModule;
import me.qyh.entity.tag.WebTag;
import me.qyh.entity.tag.WebTagCount;

public interface WebTagCountDao extends BaseDao<WebTagCount, Integer> {

	void updateCount(@Param("wt") WebTag wt, @Param("module") TagModule module, @Param("count") int count);

	List<WebTagCount> selectByTag(WebTag tag);
}
