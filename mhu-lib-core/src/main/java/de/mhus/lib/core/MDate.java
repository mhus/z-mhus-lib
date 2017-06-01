package de.mhus.lib.core;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MDate extends Date {

	private static final long serialVersionUID = 1L;
	
	private static SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static SimpleDateFormat fileDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm");
	private static SimpleDateFormat timeSecFormat = new SimpleDateFormat(
			"HH:mm:ss");
	private static SimpleDateFormat germanDateFormat = new SimpleDateFormat(
			"dd.MM.yyyy");

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
		synchronized (iso8601DateFormat) {
			iso8601DateFormat.setTimeZone(TimeZone.getDefault());
			return iso8601DateFormat.format(date);
		}
	}
	
	public static String toIso8601(Date date, TimeZone tz) {
		synchronized (iso8601DateFormat) {
			iso8601DateFormat.setTimeZone(tz);
			return iso8601DateFormat.format(date);
		}
	}
	
	public static String toDateTimeString(Date date) {
		return toDateTimeString(date, null);
	}
	
	public static String toDateTimeString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    synchronized (timeFormat) {
			timeFormat.setTimeZone(TimeZone.getDefault());
	    	return df.format(date) + " " + timeFormat.format(date);
		}
	}
	
	public static String toDateTimeString(Date date, Locale locale, TimeZone tz) {
	    DateFormat df = getLocaleDateFormater(locale);
	    synchronized (timeFormat) {
			timeFormat.setTimeZone(tz);
	    	return df.format(date) + " " + timeFormat.format(date);
		}
	}

	public static String toDateTimeSecondsString(Date date) {
		return toDateTimeSecondsString(date, null);
	}
	
	public static String toDateTimeSecondsString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    synchronized (timeSecFormat) {
			timeSecFormat.setTimeZone(TimeZone.getDefault());
	    	return df.format(date) + " " + timeSecFormat.format(date);
		}
	}

	public static String toDateTimeSecondsString(Date date, Locale locale, TimeZone tz) {
	    DateFormat df = getLocaleDateFormater(locale);
	    synchronized (timeSecFormat) {
			timeSecFormat.setTimeZone(tz);
	    	return df.format(date) + " " + timeSecFormat.format(date);
		}
	}
	
	public static String toDateString(Date date) {
		return toDateString(date, null);
	}
	
	public static String toDateString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    return df.format(date);
	}

	/**
	 * Returns a locale specific date formatter.
	 * @param locale the locale or null for default locale
	 * @return
	 */
	public static DateFormat getLocaleDateFormater(Locale locale) {
	    
		if (locale == null) locale = Locale.getDefault();
		if (locale == null || Locale.GERMANY.equals(locale))
	    	return germanDateFormat;
	    
	    int style = DateFormat.SHORT;
	    return DateFormat.getDateInstance(style, locale);
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String toFileFormat(Date date) {
		synchronized (fileDateFormat) {
			fileDateFormat.setTimeZone(TimeZone.getDefault());
			return fileDateFormat.format(date);
		}
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
		synchronized (iso8601DateFormat) {
			iso8601DateFormat.setTimeZone(TimeZone.getDefault());
			return iso8601DateFormat.format(date);
		}
	}
	
	public static String toIso8601(Calendar date, TimeZone tz) {
		synchronized (iso8601DateFormat) {
			iso8601DateFormat.setTimeZone(tz);
			return iso8601DateFormat.format(date);
		}
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param date
	 * @return
	 */
	public static String toFileFormat(Calendar date) {
		return fileDateFormat.format(date);
	}

	public static Date toDate(Object in,Date def, Locale locale) {
		return MCast.toDate(in, def, locale);
	}
	
	public static Date toDate(Object in,Date def) {
		return MCast.toDate(in, def);
	}

	public static String toTimeString(Date date) {
		synchronized (timeFormat) {
			timeFormat.setTimeZone(TimeZone.getDefault());
			return timeFormat.format(date);
		}
	}
	
	public static String toTimeString(Date date, TimeZone tz) {
		synchronized (timeFormat) {
			timeFormat.setTimeZone(tz);
			return timeFormat.format(date);
		}
	}
	
	public static String toTimeSecondsString(Date date) {
		synchronized (timeSecFormat) {
			timeSecFormat.setTimeZone(TimeZone.getDefault());
			return timeSecFormat.format(date);
		}
	}

	public static String toTimeSecondsString(Date date, TimeZone tz) {
		synchronized (timeSecFormat) {
			timeSecFormat.setTimeZone(tz);
			return timeSecFormat.format(date);
		}
	}
	
	public static String toString(String format, Date date) {
		return new SimpleDateFormat(format).format( date );
	}
	
	public static String transform(String format, String date, Date def) {
		Date d = toDate(date, def);
		if (d == null) return null;
		return toString(format, d);
	}

	public static Date toDateOnly(Date date) {
		if (date == null) return null;
		return new Date( date.getTime() / MTimeInterval.DAY_IN_MILLISECOUNDS * MTimeInterval.DAY_IN_MILLISECOUNDS );
	}

}
