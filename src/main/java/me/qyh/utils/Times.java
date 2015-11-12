package me.qyh.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Times {

	private static final long hour = 1000 * 60 * 60;
	private static final long minute = 1000 * 60;
	private static final long second = 1000;

	public static double getMinute(Date small, Date large) {
		long time = large.getTime() - small.getTime();
		return time / minute;
	}

	public static double getSecond(Date small, Date large) {
		long time = large.getTime() - small.getTime();
		return time / second;
	}

	public static double getHour(Date small, Date large) {
		long time = large.getTime() - small.getTime();
		return time / hour;
	}

	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date clean(Date toClean) {
		Calendar cal = Calendar.getInstance();
		if (toClean != null) {
			cal.setTime(toClean);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal.getTime();
	}

	/**
	 * 
	 * @param date
	 *            current date
	 * @return LIKE '2015-01-01 00:00:00'
	 */
	public static Date getFirstDayOfCurrentYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 
	 * @param date
	 *            current date
	 * @return LIKE '2016-01-01 00:00:00'
	 */
	public static Date getFirstDayOfNextYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.add(Calendar.YEAR, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	public static Date getFirstDayOfThisYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	public static Date getFirstDayOfCurrentMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date getFirstDayOfNextMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static double getSecond(long end, long start) {
		return (end - start) / second;
	}

	public static Date getPreviousDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	public static Date getNextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(clean(date));
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

}
