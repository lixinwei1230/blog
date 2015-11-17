package me.qyh.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.qyh.utils.Times;

public class DateFileIndexs {

	private Date begin;
	private Date end;
	private int count;
	private DateType dateType;

	public enum DateType {
		YEAR, MONTH
	}

	private List<DateFileIndexs> subfiles = new ArrayList<DateFileIndexs>();

	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	public int getCount() {
		return count;
	}

	public List<DateFileIndexs> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<DateFileIndexs> subfiles) {
		this.subfiles = subfiles;
	}

	public DateType getDateType() {
		return dateType;
	}

	private DateFileIndexs(DateFileIndex file, DateType type) {
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

	public static List<DateFileIndexs> buildYM(List<DateFileIndex> files) {
		List<DateFileIndexs> _files = null;
		if (!files.isEmpty()) {
			Map<Integer, List<DateFileIndexs>> filesMap = new HashMap<Integer, List<DateFileIndexs>>();
			for (DateFileIndex file : files) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(file.getDate());
				int year = cal.get(Calendar.YEAR);
				if (filesMap.containsKey(year)) {
					filesMap.get(year).add(new DateFileIndexs(file, DateType.MONTH));
				} else {
					List<DateFileIndexs> subfiles = new ArrayList<DateFileIndexs>();
					subfiles.add(new DateFileIndexs(file, DateType.MONTH));
					filesMap.put(year, subfiles);
				}
			}
			_files = new ArrayList<DateFileIndexs>(filesMap.size());
			for (Map.Entry<Integer, List<DateFileIndexs>> data : filesMap.entrySet()) {
				int year = data.getKey();
				DateFileIndex _file = new DateFileIndex();
				int count = 0;
				List<DateFileIndexs> value = data.getValue();
				Collections.sort(value, new Comparator<DateFileIndexs>() {

					@Override
					public int compare(DateFileIndexs o1, DateFileIndexs o2) {
						return o1.getBegin().compareTo(o2.getBegin());
					}

				});
				for (DateFileIndexs datas : data.getValue()) {
					count += datas.getCount();
				}
				_file.setCount(count);
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				_file.setDate(cal.getTime());
				DateFileIndexs yfs = new DateFileIndexs(_file, DateType.YEAR);
				yfs.setSubfiles(value);
				_files.add(yfs);
			}
			Collections.sort(_files, new Comparator<DateFileIndexs>() {

				@Override
				public int compare(DateFileIndexs o1, DateFileIndexs o2) {
					return -(o1.getBegin().compareTo(o2.getBegin()));
				}

			});
		}
		return _files;
	}

}