package me.qyh.service;

import me.qyh.entity.tag.UserTag;
import me.qyh.entity.tag.WebTag;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserTagPageParam;
import me.qyh.pageparam.WebTagPageParam;

public interface TagService {

	Page<WebTag> findWebTags(WebTagPageParam param);

	Page<UserTag> findUserTags(UserTagPageParam param);

}
