package me.qyh.page.widget;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.config.WidgetConfig;

public interface SystemWidgetConfigHandler {
	
	/**
	 * 得到当前widget配置项
	 * 
	 * @param widget
	 * @return
	 */
	WidgetConfig getConfig(LocationWidget widget);

	/**
	 * 储存新WidgetConfig
	 * 
	 * @param config
	 *            {@code WidgetConfig}
	 */
	void storeWidgetConfig(WidgetConfig config) throws LogicException;

	/**
	 * 删除配置的WidgetConfig
	 * 
	 * @param widget
	 * @throws LogicException
	 */
	void deleteWidgetConfig(LocationWidget widget) throws LogicException;

	/**
	 * 更新配置
	 * 
	 * @param config
	 * @throws LogicException
	 */
	void updateWidgetConfig(WidgetConfig config) throws LogicException;
	
	/**
	 * 根据configId查找config
	 * 
	 * @param id
	 * @return
	 * @throws LogicException
	 */
	WidgetConfig getConfig(Integer id) throws LogicException;
	
	/**
	 * 得到默认的html config
	 * 
	 * @param current
	 * @return
	 */
	WidgetConfig getDefaultWidgetConfig(User current);

}
