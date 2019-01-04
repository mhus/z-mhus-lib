/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import java.util.Calendar;
import java.util.Date;

/**
 * @author hummel
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MPeriod {


	public static final long MONTH_AVERAGE_MILLISECONDS = 2629746000l; // for 10.000 years
	public static final long YEAR_AVERAGE_MILLISECONDS = MONTH_AVERAGE_MILLISECONDS * 12; // for 10.000 years
	public static final long SECOUND_IN_MILLISECOUNDS = 1000;
	public static final long MINUTE_IN_MILLISECOUNDS = SECOUND_IN_MILLISECOUNDS * 60;
	public static final long HOUR_IN_MILLISECOUNDS = MINUTE_IN_MILLISECOUNDS * 60;
	public static final long DAY_IN_MILLISECOUNDS = HOUR_IN_MILLISECOUNDS * 24;
	public static final long WEEK_IN_MILLISECOUNDS = DAY_IN_MILLISECOUNDS * 7;
	
	public final static int MILLISECOND = 1;
	public final static int SECOND = 2;
	public final static int MINUTE = 3;
	public final static int HOUR = 4;
	public final static int DAY = 5;
	public final static int WEEK = 6;
	public final static int MONTH = 7;
	public final static int YEAR = 8;
	public final static int DECADE = 9;
	public final static int CENTURY = 10;
	public final static int MILLENIUM = 11;

	private long days = 0;
//	private long months = 0;
//	private long years = 0;
	private long hours = 0;
	private long minutes = 0;
	private long seconds = 0;
	private long millisec = 0;

	public MPeriod() {

	}

	public MPeriod(long millisec) {
		this.millisec = millisec;
		optimize();
	}
	
	public MPeriod(String _interval) throws NumberFormatException {
		parse(_interval);
	}

	public void parse(String _interval) throws NumberFormatException {

		if (_interval == null) return;
		
		if (_interval.indexOf(":") > 0) {
			
			add(HOUR,Integer.parseInt(MString.beforeIndex(_interval, ':')));
			_interval = MString.afterIndex(_interval, ':');
			if (_interval.indexOf(":") < 0) {
				add(MINUTE,Integer.parseInt(_interval));
			} else {
				add(MINUTE,Integer.parseInt(MString.beforeIndex(_interval, ':')));
				add(SECOND,Integer.parseInt(MString.afterIndex(_interval, ':')));
			}
			optimize();
			return;
		}
		
		StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < _interval.length(); i++) {

			char c = _interval.charAt(i);
			switch (c) {

//			case 'y':
//				add(YEAR, Integer.parseInt(buffer.toString()));
//				buffer.setLength(0);
//				break;
//			case 'm':
//				add(MONTH, Integer.parseInt(buffer.toString()));
//				buffer.setLength(0);
//				break;
			case 'w':
				add(WEEK, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'd':
				add(DAY, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'h':
				add(HOUR, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'M':
				add(MINUTE, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 's':
				add(SECOND, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'S':
				add(MILLISECOND, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'm':
				add(MONTH, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'y':
				add(YEAR, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'c':
				add(CENTURY, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case 'x':
				add(MILLENIUM, Integer.parseInt(buffer.toString()));
				buffer.setLength(0);
				break;
			case ' ':
			case '\t':
				// ignore
				break;
			case '\n':
			case '\r':
				// finish parsing
				optimize();
				return;
			default:
				// add to buffer
				buffer.append(c);
			}

		}

		optimize();
	}

	public void add(MPeriod interval) {
		if (interval == null) return;
		parse(interval.toString());
	}
	
	public void add(int _type, long _value) {

		switch (_type) {

		case YEAR:
			millisec += _value * YEAR_AVERAGE_MILLISECONDS;
			break;
		case MONTH:
			millisec += _value * MONTH_AVERAGE_MILLISECONDS;
			break;
		case WEEK:
			days += _value*7;
			break;
		case DAY:
			days += _value;
			break;
		case HOUR:
			hours += _value;
			break;
		case MINUTE:
			minutes += _value;
			break;
		case SECOND:
			seconds += _value;
			break;
		case MILLISECOND:
			millisec += _value;
			break;
		case DECADE:
			millisec += _value * 10 * YEAR_AVERAGE_MILLISECONDS;
			break;
		case CENTURY:
			millisec += _value * 100 * YEAR_AVERAGE_MILLISECONDS;
			break;
		case MILLENIUM:
			millisec += _value * 1000 * YEAR_AVERAGE_MILLISECONDS;
			break;
		}

	}

	public void optimize() {

		if (millisec >= 1000) {
			seconds += millisec / 1000;
			millisec = millisec % 1000;
		}
		if (seconds >= 60) {
			minutes += seconds / 60;
			seconds = seconds % 60;
		}
		if (minutes >= 60) {
			hours += minutes / 60;
			minutes = minutes % 60;
		}
		if (hours >= 24) {
			days += hours / 24;
			hours = hours % 24;
		}

//		if (months >= 12) {
//			years += months / 12;
//			months = months % 12;
//		}

		if (millisec <= -1000) {
			seconds += millisec / 1000;
			millisec = -((-millisec) % 1000);
		}
		if (seconds <= -60) {
			minutes += seconds / 60;
			seconds = -((-seconds) % 60);
		}
		if (minutes <= -60) {
			hours += minutes / 60;
			minutes = -((-minutes) % 60);
		}
		if (hours <= -24) {
			days += hours / 24;
			hours = -((-hours) % 24);
		}

//		if (months <= -12) {
//			years += months / 12;
//			months = -((-months) % 12);
//		}

	}

	public Date join(Date _date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		join(cal);
		return cal.getTime();
	}

	public void join(Calendar _cal) {

		_cal.add(Calendar.MILLISECOND, (int)millisec);
		_cal.add(Calendar.SECOND, (int)seconds);
		_cal.add(Calendar.MINUTE, (int)minutes);
		_cal.add(Calendar.HOUR, (int)hours);
		_cal.add(Calendar.DATE, (int)days);
//		_cal.add(Calendar.MONTH, (int)months);
//		_cal.add(Calendar.YEAR, (int)years);

	}

	@Override
	public String toString() {
		return "" + days + "d "
				+ hours + "h " + minutes + "M " + seconds + "s " + millisec
				+ "S";
//		return "" + years + "y " + months + "m " + weeks + "w " + days + "d "
//		+ hours + "h " + minutes + "M " + seconds + "s " + millisec
//		+ "S";
	}

	/**
	 * Parse a string and returns an interval, possible formats are
	 * Secounds: 123425
	 * Secounds and millis: 12345.123
	 * Format: Day Hour:Minutes:Secounds.Millis or DD HH:MM:ss.SSS
	 * or ss, ss.SSS, MM:ss.SSS, HH:MM:ss.SSS, MM:ss, HH:MM:ss
	 * 
	 * @param interval
	 * @param def
	 * @return milliseconds
	 */
	public static long toMilliseconds(String interval, long def) {
		if (interval == null) return def;
		try {
			// parse string
			String msec = null;
			String sec = null;
			String min = null;
			String hour = null;
			String day = null;
			
			if (interval.indexOf(':') > 0) {
				String[] parts = MString.split(interval, ":");
				if (parts.length == 3) {
					hour = parts[0];
					min = parts[1];
					sec = parts[2];
				} else
				if (parts.length == 2) {
					min = parts[0];
					sec = parts[1];
				}
			} else
				sec = interval;
			
			if (hour != null && hour.indexOf(' ') > 0) {
				day = MString.beforeIndex(hour, ' ');
				hour = MString.afterIndex(hour, ' ');
			}
			
			if (sec != null && sec.indexOf('.') > 0) {
				msec = MString.afterIndex(sec, '.');
				sec = MString.beforeIndex(sec, '.');
			}
			
			long out = 0;

			if (day != null)
				out = out + MCast.tolong(day, 0) * DAY_IN_MILLISECOUNDS;
			
			if (hour != null)
				out = out + MCast.tolong(hour,0) * HOUR_IN_MILLISECOUNDS;
			
			if (min != null)
				out = out + MCast.tolong(min, 0) * MINUTE_IN_MILLISECOUNDS;
			
			if (sec != null)
				out = out + MCast.tolong(sec, 0) * SECOUND_IN_MILLISECOUNDS;
			
			if (msec != null)
				out = out + MCast.tolong(msec, 0);

			return out;
			
		} catch (Throwable t) {
 			
		}
		return def;
	}

	public int getMilliseconds() {
		return (int)millisec;
	}
	
	public int getSeconds() {
		return (int)seconds;
	}
	
	public int getMinutes() {
		return (int)minutes;
	}
	
	public int getHours() {
		return (int)hours;
	}
	
	public int getDays() {
		return (int)days;
	}
		
//	public int getMonths() {
//		return (int)months;
//	}
//	
//	public int getYears() {
//		return (int)years;
//	}
	
	public long getAllMilliseconds() {
		return ( ( ( days * 24  + hours) * 60 + minutes) * 60 + seconds) * 1000 + millisec;
	}

	public long getAllSecounds() {
		return getAllMilliseconds() / 1000;
	}

	public long getAllMinutes() {
		return getAllMilliseconds() / 1000 / 60;
	}
	
	public long getAllHours() {
		return getAllMilliseconds() / 1000 / 60 / 60;
	}
	
	public long getAllDays() {
		return getAllMilliseconds() / 1000 / 60 / 60 / 24;
	}
	
	public long getAllWeeks() {
		return getAllMilliseconds() / 1000 / 60 / 60 / 24 / 7;
	}
	
	public long getAverageMonths() {
		return getAllMilliseconds() / MONTH_AVERAGE_MILLISECONDS;
	}

	public long getAverageYears() {
		return getAllMilliseconds() / YEAR_AVERAGE_MILLISECONDS;
	}
	
	public static boolean isTimeOut(long start, long stop, long timeout) {
		return timeout > -1 && stop - start > timeout;
	}
	
	public static boolean isTimeOut(long start, long timeout) {
		return timeout > -1 && System.currentTimeMillis() - start > timeout;
	}

	public static long toTime(String in, long def) {
		if (in == null) return def;
		in = in.trim().toLowerCase();
		if (in.endsWith("M") || in.endsWith("min") || in.endsWith("minutes") || in.endsWith("minute"))
			return MCast.tolong( MString.integerPart(in) , 0) * MINUTE_IN_MILLISECOUNDS;

		if (in.endsWith("h") || in.endsWith("hour") || in.endsWith("hours")) 
			return MCast.tolong( MString.integerPart(in) , 0) * HOUR_IN_MILLISECOUNDS;
		
		if (in.endsWith("s") || in.endsWith("sec") || in.endsWith("secound") || in.endsWith("secounds")) 
			return MCast.tolong( MString.integerPart(in) , 0) * SECOUND_IN_MILLISECOUNDS;
		
		if (in.endsWith("d") || in.endsWith("day") || in.endsWith("days")) 
			return MCast.tolong( MString.integerPart(in) , 0) * DAY_IN_MILLISECOUNDS;

		if (in.endsWith("w") || in.endsWith("week") || in.endsWith("weeks") ) 
			return MCast.tolong( MString.integerPart(in) , 0) * DAY_IN_MILLISECOUNDS * 7;
		
		if (in.endsWith("m") || in.endsWith("mon") || in.endsWith("month") || in.endsWith("months")) 
			return MCast.tolong( MString.integerPart(in) , 0) * MONTH_AVERAGE_MILLISECONDS;
		
		if (in.endsWith("y") || in.endsWith("year") || in.endsWith("years")) 
			return MCast.tolong( MString.integerPart(in) , 0) * YEAR_AVERAGE_MILLISECONDS;
		
		return MCast.tolong(in, def);
	}

	public static String getIntervalAsString(long msec) {
		
		boolean negative = false;
		if (msec < 0) {
			negative = true;
			msec = -msec;
		}
		
		long sec = msec / 1000;
		long min = sec / 60;
		long hours = min / 60;
		long days = hours / 24;
		long months = msec / MINUTE_IN_MILLISECOUNDS % 12;
		long years = msec / YEAR_AVERAGE_MILLISECONDS;
		
		return 
				(negative ? "-" : "") 
				+ (years > 0 ? MCast.toString(years) + "y " : "")
				+ (years > 0 || months > 0 ? MCast.toString(months) + "m " : "")
				+ MCast.toString( (int) (days % 365), 2) + ' '
				+ MCast.toString((int) (hours % 24), 2) + ':'
				+ MCast.toString((int) (min % 60), 2) + ':'
				+ MCast.toString((int) (sec % 60), 2) + '.'
				+ MCast.toString((int) (msec % 1000), 3);

	}

	public static String getIntervalAsStringSec(long msec) {
		long sec = msec / 1000;
		long min = sec / 60;
		long hours = min / 60;
		long days = hours / 24;
		long months = msec / MINUTE_IN_MILLISECOUNDS % 12;
		long years = msec / YEAR_AVERAGE_MILLISECONDS;
		
		return 
				(years > 0 ? MCast.toString(years) + "y " : "")
				+ (years > 0 || months > 0 ? MCast.toString(months) + "m " : "")
				+ MCast.toString( (int) (days % 365), 2) + ' '
				+ MCast.toString((int) (hours % 24), 2) + ':'
				+ MCast.toString((int) (min % 60), 2) + ':'
				+ MCast.toString((int) (sec % 60), 2);

	}
	
	public static String getIntervalAsStringMin(long msec) {
		long sec = msec / 1000;
		long min = sec / 60;
		long hours = min / 60;
		long days = hours / 24;
		long months = msec / MINUTE_IN_MILLISECOUNDS % 12;
		long years = msec / YEAR_AVERAGE_MILLISECONDS;
		
		return 
				(years > 0 ? MCast.toString(years) + "y " : "")
				+ (years > 0 || months > 0 ? MCast.toString(months) + "m " : "")
				+ MCast.toString( (int) (days % 365), 2) + ' '
				+ MCast.toString((int) (hours % 24), 2) + ':'
				+ MCast.toString((int) (min % 60), 2);

	}
	
}
