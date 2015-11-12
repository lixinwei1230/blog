package me.qyh.dao;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.CommentScope;

public interface CommentScopeDao extends BaseDao<CommentScope, Integer> {

	CommentScope selectByScopeAndScopeId(@Param("scope") String scope, @Param("scopeId") String scopeId);

}
