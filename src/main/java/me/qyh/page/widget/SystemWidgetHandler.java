package me.qyh.page.widget;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
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
	 * @throws LogicException
	 */
	Widget getWidget(WidgetConfig config, User owner, User visitor) throws LogicException;

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
	Widget getSimpleWidget();

	/**
	 * 得到用用来预览的widget
	 */
	Widget getPreviewWidget(User current);

	SystemWidgetConfigHandler getConfigHandler();

}
