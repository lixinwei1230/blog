package me.qyh.helper.cache;

public class Sign implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long first;
	private int total;
	private int periodSec;// 时间
	private int hits;// period时间内点击hits次即达成缓存条件

	public boolean addHit(long time) {
		if ((time - first) / 1000 > periodSec) {
			return false;
		}
		total ++ ;
		return true;
	}

	public boolean cache(long time) {
		return (time - first) / 1000 <= periodSec && total >= hits;
	}

	public Sign(int periodSec, int hits) {
		this.periodSec = periodSec;
		this.hits = hits;
		this.first = System.currentTimeMillis();
		this.total = 1;
	}
	
	public Sign(){
		
	}

}
