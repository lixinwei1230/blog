package me.qyh.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客日期归档
 * 
 * @author mhlx
 *
 */
public class DateFileIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date date;// 日期 yyyy-MM
	private int count;// 某日期下博客数量

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
