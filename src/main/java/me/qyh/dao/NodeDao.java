package me.qyh.dao;

import java.io.Serializable;

public interface NodeDao<T, PK extends Serializable> extends BaseDao<T, PK> {

	int updateNodesLftWhenInsert(T node);

	int updateNodesRgtWhenInsert(T node);

	int updateNodesLftWhenDelete(T node);

	int updateNodesRgtWhenDelete(T node);

	int selectNodeLayer(T node);

	int deleteNodes(T node);

}
