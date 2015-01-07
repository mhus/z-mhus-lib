/*
 * ./core/de/mhu/lib/ATimeInterval.java
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
public class MTimeInterval {

	
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
//	public final static int MONTH = 7;
//	public final static int YEAR = 8;
//	public final static int DECADE = 9;
//	public final static int CENTURY = 10;
//	public final static int MILLENIUM = 11;

	private long days = 0;
	private long weeks = 0;
//	private long months = 0;
//	private long years = 0;
	private long hours = 0;
	private long minutes = 0;
	private long seconds = 0;
	private long millisec = 0;

	public MTimeInterval() {

	}

	public MTimeInterval(String _interval) throws NumberFormatException {
		this();
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
		
		StringBuffer buffer = new StringBuffer();

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

	public void add(MTimeInterval interval) {
		if (interval == null) return;
		parse(interval.toString());
	}
	
	public void add(int _type, long _value) {

		switch (_type) {

//		case YEAR:
//			years += _value;
//			break;
//		case MONTH:
//			months += _value;
//			break;
		case WEEK:
			weeks += _value;
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
//		case DECADE:
//			years += _value * 10;
//			break;
//		case CENTURY:
//			years += _value * 100;
//			break;
//		case MILLENIUM:
//			years += _value * 1000;
//			break;
		}

	}

	public void optimize() {

		if (millisec >= 100) {
			seconds += millisec / 100;
			millisec = millisec % 100;
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
		if (days >= 7) {
			weeks += weeks / 7;
			days = days % 7;
		}

//		if (months >= 12) {
//			years += months / 12;
//			months = months % 12;
//		}

		if (millisec <= -100) {
			seconds += millisec / 100;
			millisec = -((-millisec) % 100);
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
		if (days <= -7) {
			weeks += weeks / 7;
			days = -((-days) % 7);
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
		_cal.add(Calendar.DATE, (int)(days + weeks * 7));
//		_cal.add(Calendar.MONTH, (int)months);
//		_cal.add(Calendar.YEAR, (int)years);

	}

	public String toString() {
		return "" + weeks + "w " + days + "d "
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
	 * @return
	 */
	public static long toMilliseconds(String interval, int def) {
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
	
	public int getWeeks() {
		return (int)weeks;
	}
	
//	public int getMonths() {
//		return (int)months;
//	}
//	
//	public int getYears() {
//		return (int)years;
//	}
	
	public long getAllMilliseconds() {
		return ( ( ( ( weeks * 7 ) + days * 24 ) + minutes * 60 ) + seconds * 1000 ) + millisec;
	}

	public long getAllSecounds() {
		return getAllMilliseconds() / 1000;
	}

	public long getAllMinutes() {
		return getAllMilliseconds() / 1000 / 60;
	}
	
	public long getAllDays() {
		return getAllMilliseconds() / 1000 / 60 / 24;
	}
	
	public long getAllWeeks() {
		return getAllMilliseconds() / 1000 / 60 / 24 / 7;
	}

}
