package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.page.Page;
import me.qyh.page.PageType;

public interface PageDao extends BaseDao<Page, Integer> {

	List<Page> selectByPageType(@Param("type") PageType type,
			@Param("user") User user);

}
