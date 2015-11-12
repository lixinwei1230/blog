package me.qyh.helper.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 行容器，每个行容器可以包含多个列容器
 * 
 * @author henry.qian
 *
 */
public class RowContainer implements Comparable<RowContainer> {

	private int index;// 第几个行容器
	private List<ColumnContainer> columns = new ArrayList<ColumnContainer>();

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ColumnContainer> getColumns() {
		return columns;
	}

	@Override
	public int compareTo(RowContainer o) {
		return index - o.index;
	}

	protected ColumnContainer seekColumnContainer(int index) {
		for (ColumnContainer cc : columns) {
			if (cc.getIndex() == index) {
				return cc;
			}
		}
		return null;
	}

	protected RowContainer addColumnContainer(ColumnContainer cc) {
		if (seekColumnContainer(cc.getIndex()) == null) {
			columns.add(cc);
			Collections.sort(columns);
		}
		return this;
	}

}
