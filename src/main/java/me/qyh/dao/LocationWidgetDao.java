package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.page.LocationWidget;
import me.qyh.page.Page;
import me.qyh.page.widget.Widget;

public interface LocationWidgetDao extends BaseDao<LocationWidget, Integer> {

	LocationWidget selectByIndex(@Param("rowIndex") int rowIndex, @Param("columnIndex") int columnIndex,
			@Param("index") int index, @Param("page") Page page);

	List<LocationWidget> selectByPage(Page page);

	LocationWidget selectByWidgetAndPage(@Param("widget") Widget widget, @Param("page") Page page);

	List<LocationWidget> selectByWidget(Widget widget);

	int selectCountByPage(Page page);

}
