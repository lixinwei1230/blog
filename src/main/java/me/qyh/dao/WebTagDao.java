package me.qyh.dao;

import java.util.List;

import me.qyh.entity.tag.WebTag;
import me.qyh.pageparam.WebTagPageParam;

public interface WebTagDao extends BaseDao<WebTag, Integer> {

	WebTag selectByName(String name);

	int selectCount(WebTagPageParam param);

	List<WebTag> selectPage(WebTagPageParam param);

}
