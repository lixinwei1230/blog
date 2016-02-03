package me.qyh.pageparam;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页
 * 
 * @author mhlx
 *
 * @param <T>
 */
public class Page<T> {

	private List<T> datas = new ArrayList<T>();
	private int totalPage;// 总页数
	private int offset;
	private int currentPage;// 当前页
	private int liststep = 10;
	private int listbegin;
	private int listend;
	private int pageSize;// 每页记录数
	private int totalRow;// 总记录数

	public Page() {

	}

	public Page(int pageSize, int offset, int totalRow, List<T> datas) {
		this.pageSize = pageSize;
		this.offset = offset;
		this.currentPage = offset / pageSize + 1;
		this.totalRow = totalRow;
		this.totalPage = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
		countListbeginAndListend();
		this.datas = datas;
	}

	public Page(PageParam param, int totalRow, List<T> datas) {
		this(param.getPageSize(), param.getOffset(), totalRow, datas);
	}

	private void countListbeginAndListend() {
		int listbegin = (currentPage - (int) Math.ceil((double) liststep / 2));
		listbegin = listbegin < 1 ? 1 : listbegin;
		int listend = currentPage + liststep / 2;
		if (listend > totalPage) {
			listend = totalPage + 1;
		}
		int cha = listend - listbegin + 1 - liststep;
		if (cha <= 0) {
			if (currentPage + liststep / 2 > totalPage) {
				listbegin = listbegin + cha - 1;
				if (listbegin <= 0) {
					listbegin = 1;
				}
			} else {
				listend = listend - cha + 1;
				if (listend > totalPage) {
					listend = totalPage + 1;
				}
			}
		}
		this.listbegin = listbegin;
		this.listend = listend;
	}

	public List<T> getDatas() {
		return datas;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getOffset() {
		return offset;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getListstep() {
		return liststep;
	}

	public int getListbegin() {
		return listbegin;
	}

	public int getListend() {
		return listend;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public static Integer countOffset(Integer currentPage, Integer pageSize) {
		return pageSize * (currentPage - 1);
	}

	public static Integer countCurrentPage(Integer offset, Integer pageSize) {
		return offset / pageSize + 1;
	}
	
}
