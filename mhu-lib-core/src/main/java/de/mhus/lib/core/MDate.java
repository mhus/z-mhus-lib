package de.mhus.lib.core;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MDate extends Date {

	private static final long serialVersionUID = 1L;
	
	private static SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static SimpleDateFormat fileDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public MDate() {
		super();
	}

	public MDate(long date) {
		super(date);
	}

	public MDate(Timestamp timestamp) {
		this(timestamp.getTime());
	}

	public MDate(String string) {
		Date date = MCast.toDate(string, null);
		this.setTime(date.getTime());
	}

	@Override
	public String toString() {
		return toIso8601(this);
	}

	public java.sql.Date toSqlDate() {
		return new java.sql.Date(getTime());
	}

	public Time toSqlTime() {
		return new java.sql.Time(getTime());
	}

	public Timestamp toSqlTimestamp() {
		return new java.sql.Timestamp(getTime());
	}

	public Calendar toCalendar() {
		return MCast.toCalendar(this);
	}

	public static boolean isEarlierAs(Date a, Date b) {
		return a.compareTo(b) < 0;
	}
	
	public static boolean isLaterAs(Date a, Date b) {
		return a.compareTo(b) > 0;
	}
	
	public static boolean isWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return ((dayOfWeek >= Calendar.MONDAY) && (dayOfWeek <= Calendar.FRIDAY));
	}

	public static boolean isWeekend(Date date) {
		return !isWorkDay(date);
	}
	
	/**
	 * yyyy-MM-ddTHH:mm:ss
	 * @param date
	 * @return
	 */
	public static String toIso8601(Date date) {
		return iso8601DateFormat.format(date);
	}
	
	public static String toLocaleString(Date date, boolean longFormat) {
		return toLocaleString(date, Locale.getDefault(), longFormat);
	}
	
	public static String toLocaleString(Date date, Locale locale, boolean longFormat) {
	    int style = longFormat ? DateFormat.LONG : DateFormat.MEDIUM;
	    DateFormat df = DateFormat.getDateInstance(style, locale);
	    return df.format(date);
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String toFileFormat(Date date) {
		return fileDateFormat.format(date);
	}
	
	/**
	 * Returns the date in iso format: yyyy-mm-dd
	 * 
	 * @param _in
	 * @return
	 */
	public static String toIsoDate(Date _in) {
		Calendar c = Calendar.getInstance();
		c.setTime(_in);
		return toIsoDate(c);
	}

	/**
	 * Returns the date in iso time format: yyyy-mm-dd HH:mm:ss.SSS
	 * 
	 * @param _in
	 * @return
	 */
	public static String toIsoDateTime(Date _in) {
		Calendar c = Calendar.getInstance();
		c.setTime(_in);
		return toIsoDateTime(c);
	}

	public static String toIsoDateTime(Calendar _in) {
		return _in.get(Calendar.YEAR) + "-"
				+ MCast.toString(_in.get(Calendar.MONTH) + 1, 2) + "-"
				+ MCast.toString(_in.get(Calendar.DAY_OF_MONTH), 2) + " "
				+ MCast.toString(_in.get(Calendar.HOUR_OF_DAY), 2) + ":"
				+ MCast.toString(_in.get(Calendar.MINUTE), 2) + ":"
				+ MCast.toString(_in.get(Calendar.SECOND), 2) 
				// + "." + toString(_in.get(Calendar.MILLISECOND), 3)
				;
	}

	/**
	 * Calendar to iso date: yyyy-mm-dd
	 * @param timeStamp 
	 * 
	 * @param _in
	 * @return
	 */
	public static String toIsoDateTime(long timeStamp) {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp);
		return toIsoDateTime(c);
		
	}
	
	/**
	 * Calendar to iso date: yyyy-mm-dd
	 * 
	 * @param _in
	 * @return
	 */
	public static String toIsoDate(Calendar _in) {
		return _in.get(Calendar.YEAR) + "-"
				+ MCast.toString(_in.get(Calendar.MONTH) + 1, 2) + "-"
				+ MCast.toString(_in.get(Calendar.DAY_OF_MONTH), 2);
	}

	/**
	 * yyyy-MM-ddTHH:mm:ss
	 * @param date
	 * @return
	 */
	public static String toIso8601(Calendar date) {
		return iso8601DateFormat.format(date);
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String toFileFormat(Calendar date) {
		return fileDateFormat.format(date);
	}

	public static Date toDate(Object in,Date def) {
		return MCast.toDate(in, def);
	}

}
