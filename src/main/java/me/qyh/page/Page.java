package me.qyh.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.qyh.entity.Id;
import me.qyh.entity.User;

/**
 * 行列式布局
 * 
 * @author henry.qian
 *
 */
public class Page extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private List<LocationWidget> widgets = new ArrayList<LocationWidget>();
	private PageType type;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<LocationWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<LocationWidget> widgets) {
		this.widgets = widgets;
	}

	public void addLocationWidgets(LocationWidget... widgets) {
		Collections.addAll(this.widgets, widgets);
	}

	public Page() {

	}

	public PageType getType() {
		return type;
	}

	public void setType(PageType type) {
		this.type = type;
	}

	public Page(User user, PageType type) {
		this.user = user;
		this.type = type;
	}

}
