package me.qyh.helper.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.qyh.entity.Id;
import me.qyh.page.LocationWidget;
import me.qyh.page.Page;

/**
 * 简单的bootstrap页面
 * 
 * @author henry.qian
 *
 */
public class SimpleBootstrapPage extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<RowContainer> rows = new ArrayList<RowContainer>();

	public List<RowContainer> getRows() {
		return rows;
	}

	public SimpleBootstrapPage(Page page) {
		super(page.getId());
		for (LocationWidget lw : page.getWidgets()) {
			int rIndex = lw.getR();
			int cIndex = lw.getX();
			RowContainer rc = seekRowContainer(rIndex);
			if (rc == null) {
				RowContainer newR = new RowContainer();
				ColumnContainer newC = new ColumnContainer();
				newC.setIndex(cIndex);
				newC.mergeWidget(lw);
				newR.setIndex(rIndex);
				newR.addColumnContainer(newC);
				this.addRowContainer(newR);
			} else {
				ColumnContainer cc = rc.seekColumnContainer(cIndex);
				if (cc == null) {
					ColumnContainer newC = new ColumnContainer();
					newC.setIndex(cIndex);
					newC.mergeWidget(lw);
					rc.addColumnContainer(newC);
				} else {
					cc.mergeWidget(lw);
				}
			}
		}
	}

	private RowContainer seekRowContainer(int index) {
		for (RowContainer rc : rows) {
			if (rc.getIndex() == index) {
				return rc;
			}
		}
		return null;
	}

	public SimpleBootstrapPage addRowContainer(RowContainer rc) {
		if (seekRowContainer(rc.getIndex()) == null) {
			rows.add(rc);
			Collections.sort(rows);
		}
		return this;
	}

}
