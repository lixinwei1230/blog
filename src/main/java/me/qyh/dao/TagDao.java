package me.qyh.dao;

import me.qyh.entity.tag.Tag;

public interface TagDao extends BaseDao<Tag, Integer> {

	Tag selectByName(String name);

}
