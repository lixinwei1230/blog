package me.qyh.entity;

import java.util.Date;

import me.qyh.utils.Times;

public class MyFileIndex {

	private Date monthDate;
	private int total;

	public Date getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(Date monthDate) {
		this.monthDate = monthDate;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Date getFirstDayOfCurrentMonth() {
		return Times.getFirstDayOfCurrentMonth(monthDate);
	}

	public Date getFirstDayOfNextMonth() {
		return Times.getFirstDayOfNextMonth(monthDate);
	}
}
