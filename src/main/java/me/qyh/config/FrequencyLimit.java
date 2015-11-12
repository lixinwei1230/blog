package me.qyh.config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在begin到end这段时间内最多能操作limit次
 * 
 * @author mhlx
 *
 */
public class FrequencyLimit {

	private Date begin;
	private Date end;
	private int limit;

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public int getLimit() {
		return limit;
	}

	public FrequencyLimit(Date begin, Date end, int limit) {
		super();
		this.begin = begin;
		this.end = end;
		this.limit = limit;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "FrequencyLimit [begin=" + sdf.format(begin) + ", end="
				+ sdf.format(end) + ", limit=" + limit + "]";
	}

}
