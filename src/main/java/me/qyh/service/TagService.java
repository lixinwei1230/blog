package me.qyh.service;

import me.qyh.entity.tag.UserTag;
import me.qyh.pageparam.Page;
import me.qyh.pageparam.UserTagPageParam;

public interface TagService {

	Page<UserTag> findUserTags(UserTagPageParam param);

}
