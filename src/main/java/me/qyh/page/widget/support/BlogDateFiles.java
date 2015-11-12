package me.qyh.page.widget.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.qyh.bean.BlogDateFile;
import me.qyh.utils.Times;

public class BlogDateFiles {

	private Date begin;
	private Date end;
	private int count;
	private DateType dateType;

	public enum DateType {
		YEAR, MONTH
	}

	private List<BlogDateFiles> subfiles = new ArrayList<BlogDateFiles>();

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public int getCount() {
		return count;
	}

	public List<BlogDateFiles> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<BlogDateFiles> subfiles) {
		this.subfiles = subfiles;
	}

	public DateType getDateType() {
		return dateType;
	}

	public BlogDateFiles(BlogDateFile file, DateType type) {
		Date date = file.getDate();
		switch (type) {
		case YEAR:
			this.begin = Times.getFirstDayOfThisYear(date);
			this.end = Times.getFirstDayOfNextYear(date);
			break;
		default:
			this.begin = Times.getFirstDayOfCurrentMonth(date);
			this.end = Times.getFirstDayOfNextMonth(date);
			break;
		}
		this.count = file.getCount();
		this.dateType = type;
	}

}