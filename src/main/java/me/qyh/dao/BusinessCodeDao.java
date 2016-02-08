package me.qyh.dao;

import me.qyh.entity.BusinessCode;
import me.qyh.entity.BusinessCodeType;

import org.apache.ibatis.annotations.Param;

public interface BusinessCodeDao extends BaseDao<BusinessCode, Integer>{
	
	BusinessCode selectByCode(@Param("code") String code,@Param("type") BusinessCodeType type);

}
