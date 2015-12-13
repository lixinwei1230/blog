package me.qyh.dao;

import java.util.List;

import me.qyh.entity.LoginInfo;
import me.qyh.pageparam.LoginInfoPageParam;

public interface LoginInfoDao extends BaseDao<LoginInfo,Integer>{
	
	int selectCount(LoginInfoPageParam param);
	
	List<LoginInfo> selectPage(LoginInfoPageParam param);

}
