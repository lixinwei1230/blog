package me.qyh.page.widget;

import me.qyh.entity.User;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.page.LocationWidget;
import me.qyh.page.widget.config.WidgetConfig;

/**
 * 适用于系统widget
 * 
 * @author henry.qian
 *
 */
public interface SystemWidgetHandler {

	/**
	 * 得到用户放置的挂件内容
	 * 
	 * @param current
	 * @return
	 * @throws DataNotFoundException
	 */
	SystemWidget getWidget(LocationWidget widget, User owner, User visitor)
			throws DataNotFoundException;

	/**
	 * 得到当前widget可供配置项
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
	 * @throws DataNotFoundException
	 */
	WidgetConfig getConfig(Integer id) throws DataNotFoundException;

	/**
	 * 验证用户是否具有操作该widget的权限
	 * 
	 * @param current
	 */
	boolean doAuthencation(User current);

	/**
	 * 得到只包含id,name的widget，用于展示widgets
	 * 
	 * @return
	 */
	SystemWidget getSimpleWidget();

	/**
	 * 得到用用来预览的widget
	 */
	SystemWidget getPreviewWidget(User current);

	/**
	 * 得到默认的html config
	 * 
	 * @param current
	 * @return
	 */
	WidgetConfig getDefaultWidgetConfig(User current);

}
