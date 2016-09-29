package de.mhus.lib.core;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <p>MDate class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
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

	/**
	 * <p>Constructor for MDate.</p>
	 */
	public MDate() {
		super();
	}

	/**
	 * <p>Constructor for MDate.</p>
	 *
	 * @param date a long.
	 */
	public MDate(long date) {
		super(date);
	}

	/**
	 * <p>Constructor for MDate.</p>
	 *
	 * @param timestamp a {@link java.sql.Timestamp} object.
	 */
	public MDate(Timestamp timestamp) {
		this(timestamp.getTime());
	}

	/**
	 * <p>Constructor for MDate.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public MDate(String string) {
		Date date = MCast.toDate(string, null);
		this.setTime(date.getTime());
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return toIso8601(this);
	}

	/**
	 * <p>toSqlDate.</p>
	 *
	 * @return a {@link java.sql.Date} object.
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(getTime());
	}

	/**
	 * <p>toSqlTime.</p>
	 *
	 * @return a {@link java.sql.Time} object.
	 */
	public Time toSqlTime() {
		return new java.sql.Time(getTime());
	}

	/**
	 * <p>toSqlTimestamp.</p>
	 *
	 * @return a {@link java.sql.Timestamp} object.
	 */
	public Timestamp toSqlTimestamp() {
		return new java.sql.Timestamp(getTime());
	}

	/**
	 * <p>toCalendar.</p>
	 *
	 * @return a {@link java.util.Calendar} object.
	 */
	public Calendar toCalendar() {
		return MCast.toCalendar(this);
	}

	/**
	 * <p>isEarlierAs.</p>
	 *
	 * @param a a {@link java.util.Date} object.
	 * @param b a {@link java.util.Date} object.
	 * @return a boolean.
	 */
	public static boolean isEarlierAs(Date a, Date b) {
		return a.compareTo(b) < 0;
	}
	
	/**
	 * <p>isLaterAs.</p>
	 *
	 * @param a a {@link java.util.Date} object.
	 * @param b a {@link java.util.Date} object.
	 * @return a boolean.
	 */
	public static boolean isLaterAs(Date a, Date b) {
		return a.compareTo(b) > 0;
	}
	
	/**
	 * <p>isWorkDay.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a boolean.
	 */
	public static boolean isWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return ((dayOfWeek >= Calendar.MONDAY) && (dayOfWeek <= Calendar.FRIDAY));
	}

	/**
	 * <p>isWeekend.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a boolean.
	 */
	public static boolean isWeekend(Date date) {
		return !isWorkDay(date);
	}
	
	/**
	 * yyyy-MM-ddTHH:mm:ss
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIso8601(Date date) {
		return iso8601DateFormat.format(date);
	}
	
	/**
	 * <p>toDateTimeString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateTimeString(Date date) {
		return toDateTimeString(date, null);
	}
	
	/**
	 * <p>toDateTimeString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateTimeString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    return df.format(date) + " " + timeFormat.format(date);
	}
	

	/**
	 * <p>toDateTimeSecondsString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateTimeSecondsString(Date date) {
		return toDateTimeSecondsString(date, null);
	}
	
	/**
	 * <p>toDateTimeSecondsString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateTimeSecondsString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    return df.format(date) + " " + timeSecFormat.format(date);
	}

	/**
	 * <p>toDateString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateString(Date date) {
		return toDateString(date, null);
	}
	
	/**
	 * <p>toDateString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toDateString(Date date, Locale locale) {
	    DateFormat df = getLocaleDateFormater(locale);
	    return df.format(date);
	}

	/**
	 * Returns a locale specific date formatter.
	 *
	 * @param locale the locale or null for default locale
	 * @return a {@link java.text.DateFormat} object.
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
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toFileFormat(Date date) {
		return fileDateFormat.format(date);
	}
	
	/**
	 * Returns the date in iso format: yyyy-mm-dd
	 *
	 * @param _in a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIsoDate(Date _in) {
		Calendar c = Calendar.getInstance();
		c.setTime(_in);
		return toIsoDate(c);
	}

	/**
	 * Returns the date in iso time format: yyyy-mm-dd HH:mm:ss.SSS
	 *
	 * @param _in a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIsoDateTime(Date _in) {
		Calendar c = Calendar.getInstance();
		c.setTime(_in);
		return toIsoDateTime(c);
	}

	/**
	 * <p>toIsoDateTime.</p>
	 *
	 * @param _in a {@link java.util.Calendar} object.
	 * @return a {@link java.lang.String} object.
	 */
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
	 *
	 * @param timeStamp a long.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIsoDateTime(long timeStamp) {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeStamp);
		return toIsoDateTime(c);
		
	}
	
	/**
	 * Calendar to iso date: yyyy-mm-dd
	 *
	 * @param _in a {@link java.util.Calendar} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIsoDate(Calendar _in) {
		return _in.get(Calendar.YEAR) + "-"
				+ MCast.toString(_in.get(Calendar.MONTH) + 1, 2) + "-"
				+ MCast.toString(_in.get(Calendar.DAY_OF_MONTH), 2);
	}

	/**
	 * yyyy-MM-ddTHH:mm:ss
	 *
	 * @param date a {@link java.util.Calendar} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toIso8601(Calendar date) {
		return iso8601DateFormat.format(date);
	}
	
	/**
	 * yyyyMMddHHmmss
	 *
	 * @param date a {@link java.util.Calendar} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toFileFormat(Calendar date) {
		return fileDateFormat.format(date);
	}

	/**
	 * <p>toDate.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @param def a {@link java.util.Date} object.
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toDate(Object in,Date def, Locale locale) {
		return MCast.toDate(in, def, locale);
	}
	
	/**
	 * <p>toDate.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @param def a {@link java.util.Date} object.
	 * @return a {@link java.util.Date} object.
	 */
	public static Date toDate(Object in,Date def) {
		return MCast.toDate(in, def);
	}

	/**
	 * <p>toTimeString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toTimeString(Date date) {
		return timeFormat.format(date);
	}
	
	/**
	 * <p>toTimeSecondsString.</p>
	 *
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String toTimeSecondsString(Date date) {
		return timeSecFormat.format(date);
	}

	/**
	 * <p>toString.</p>
	 *
	 * @param format a {@link java.lang.String} object.
	 * @param date a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	public static String toString(String format, Date date) {
		return new SimpleDateFormat(format).format( date );
	}
	
	/**
	 * <p>transform.</p>
	 *
	 * @param format a {@link java.lang.String} object.
	 * @param date a {@link java.lang.String} object.
	 * @param def a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	public static String transform(String format, String date, Date def) {
		Date d = toDate(date, def);
		if (d == null) return null;
		return toString(format, d);
	}

}
