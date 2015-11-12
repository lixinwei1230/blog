package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.Space;
import me.qyh.entity.blog.BlogCategory;

public interface BlogCategoryDao extends BaseDao<BlogCategory, Integer> {

	int selectCountBySpace(Space user);

	BlogCategory selectBySpaceAndName(@Param("space") Space user,
			@Param("name") String name);

	List<BlogCategory> selectBySpace(Space space);

}
