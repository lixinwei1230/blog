package me.qyh.dao;

import java.io.Serializable;

public interface BaseDao<T, PK extends Serializable> {

	int insert(T t);

	int update(T t);

	int deleteById(PK pk);

	T selectById(PK pk);

}