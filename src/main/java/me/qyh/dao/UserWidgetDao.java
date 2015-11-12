package me.qyh.dao;

import java.util.List;

import me.qyh.entity.User;
import me.qyh.page.widget.UserWidget;

public interface UserWidgetDao extends BaseDao<UserWidget, Integer> {

	List<UserWidget> selectByUser(User user);

	int selectCountByUser(User user);

}
