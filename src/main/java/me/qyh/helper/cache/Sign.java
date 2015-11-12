package me.qyh.helper.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sign implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> periods = new ArrayList<Long>();// 点击次数
	private int periodSec;// 时间
	private int hits;// period时间内点击hits次即达成缓存条件

	private long getLastHitTime() {
		return periods.get(periods.size() - 1);
	}

	public boolean addHit(long time) {
		if ((time - getLastHitTime()) / 1000 > periodSec) {
			return false;
		}
		periods.add(time);
		return true;
	}

	public boolean cache() {
		return (getLastHitTime() - periods.get(0)) / 1000 <= periodSec && periods.size() >= hits;
	}

	public Sign(int periodSec, int hits) {
		this.periods.add(new Date().getTime());
		this.periodSec = periodSec;
		this.hits = hits;
	}

}
