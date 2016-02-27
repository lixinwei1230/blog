package me.qyh.oauth2.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.dao.BaseDao;
import me.qyh.entity.User;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.entity.OauthUser.OauthType;

public interface OauthUserDao extends BaseDao<OauthUser, Integer> {

	OauthUser selectByUserIdAndType(@Param("userid") String userid, @Param("type") OauthType type);

	OauthUser selectByUserAndType(@Param("user") User user, @Param("type") OauthType type);

	List<OauthUser> selectByUser(User user);

}
