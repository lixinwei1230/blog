package me.qyh.service;

import java.util.List;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.page.LocationWidget;
import me.qyh.page.Page;
import me.qyh.page.PageType;
import me.qyh.page.widget.UserWidget;
import me.qyh.page.widget.Widget;
import me.qyh.page.widget.WidgetType;
import me.qyh.page.widget.config.WidgetConfig;

public interface WidgetService {

	List<Widget> findWidgets(User user);

	List<UserWidget> findUserWidgets(User user);

	void putWidget(LocationWidget slw) throws LogicException;

	void removeWidget(Integer id) throws LogicException;

	Widget getPreviewWidget(Integer id, WidgetType type) throws LogicException;

	Page getPage(PageType type, User user) throws LogicException;

	WidgetConfig getConfig(Integer locationWidgetId) throws LogicException;

	void updateConfig(WidgetConfig config) throws LogicException;

	void deleteUserWidget(Integer id) throws LogicException;

	void insertUserWidget(UserWidget userWidget) throws LogicException;

	void updateLocationWidget(LocationWidget widget, boolean wrap) throws LogicException;

	UserWidget getUserWidget(Integer id) throws LogicException;

	void updateUserWidget(UserWidget uw) throws LogicException;

}
