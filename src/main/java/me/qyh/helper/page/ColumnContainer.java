package me.qyh.helper.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.qyh.page.LocationWidget;

/**
 * 列容器
 * 
 * @author henry.qian
 *
 */
public class ColumnContainer implements Comparable<ColumnContainer> {

	private static final int maxWidth = 12;

	private int index;
	private List<LocationWidget> widgets = new ArrayList<LocationWidget>();
	private final Comparator<LocationWidget> comparator = new LocationWidgetComporator();
	private int widthP;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<LocationWidget> getWidgets() {
		return widgets;
	}

	protected ColumnContainer mergeWidget(LocationWidget widget) {
		LocationWidget seeked = null;
		for (LocationWidget _widget : widgets) {
			if (_widget.getY() == widget.getY()) {
				seeked = _widget;
			}
		}
		if (seeked == null) {
			widgets.add(widget);
		} else {
			seeked.setWidth(widget.getWidth());
			seeked.setWidget(widget.getWidget());
		}
		this.widthP = maxWidgetWidth();
		Collections.sort(widgets, comparator);
		return this;
	}

	private int maxWidgetWidth() {
		int widthP = 0;
		for (LocationWidget widget : widgets) {
			if (widget.getWidth() > widthP) {
				widthP = widget.getWidth();
			}
		}
		return widthP;
	}

	public int getWidth() {
		int width = widthP * maxWidth / 100;
		return width == 0 ? 1 : width;
	}

	public int getWidthP() {
		return widthP;
	}

	@Override
	public int compareTo(ColumnContainer o) {
		return index - o.index;
	}

	private class LocationWidgetComporator implements Comparator<LocationWidget> {

		@Override
		public int compare(LocationWidget o1, LocationWidget o2) {
			return o1.getY() - o2.getY();
		}

	}

}
