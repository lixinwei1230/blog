package me.qyh.dao;

import java.io.Serializable;

import me.qyh.page.LocationWidget;

public interface BaseWidgetConfigDao<E, PK extends Serializable> extends BaseDao<E, PK> {

	E selectByLocationWidget(LocationWidget widget);

	void deleteByLocationWidget(LocationWidget widget);

}
