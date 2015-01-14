package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

public class ObjectToCalendar implements Caster<Object,Calendar>{

	private final static Log log = Log.getLog(ObjectToCalendar.class);
	
	@Override
	public Class<? extends Calendar> getToClass() {
		return Calendar.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public Calendar cast(Object in, Calendar def) {
		if (in == null) return def;
		if (in instanceof Calendar) return (Calendar)in;
		if (in instanceof Date) {
			Calendar c = Calendar.getInstance();
			c.setTime((Date)in);
			return c;
		}
		try {
			String ins = MCast.toString(in);
			Calendar ret = toCalendar(ins);
			if (ret == null) return def;
			return ret;
		} catch (Throwable t) {
			return def;
		}
	}

	public static Calendar toCalendar(String in) {
		if (in == null) return null;
		try {
			
			Calendar c = Calendar.getInstance();
			c.clear();

			String date = in.trim();

			int hour = 0;
			int min = 0;
			int sec = 0;
			int millies = 0;
			String zone = null;

			// check if date and time
			char sep = '?';
			if ( MString.isIndex(date, '_' ) )
				sep = '_';
			else
			if ( MString.isIndex(date, ' ' ) )
				sep = ' ';
			
			if (sep != '?') {
				// found also time ... parse it !
				String time = MString.afterIndex(date, sep).trim();
				date = MString.beforeIndex(in, sep).trim();

				// zone
				char sep2 = '?';
				if ( MString.isIndex(time, ' ' ) )
					sep2 = ' ';
				else
				if ( MString.isIndex(time, '_' ) )
					sep2 = '_';
				if (sep2 != '?') {
					zone = MString.afterIndex(time, sep2);
					time = MString.beforeIndex(time, sep2);
				}

				// milliseconds
				if (MString.isIndex(time, '.')) {
					millies = toint(MString.afterIndex(time, '.'), 0);
					time = MString.beforeIndex(time, '.');
				}

				// parse time
				String[] parts = time.split("\\:");
				if (parts.length > 1) {
					hour = toint(parts[0], 0);
					min = toint(parts[1], 0);
					if (parts.length > 2)
						sec = toint(parts[2], 0);
				}

				c.set(Calendar.HOUR_OF_DAY, hour);
				c.set(Calendar.MINUTE, min);
				c.set(Calendar.MILLISECOND, sec * 1000 + millies);
				
			}

			// parse the date
			if ( date.indexOf('-') > 0) {
				// technical time 2000.12.31
				String[] parts = date.split("-");
				
				if (zone != null) {
					TimeZone tz = TimeZone.getTimeZone(zone);
					c.setTimeZone(tz);
				}
	
				if (parts.length == 3) {
						int year = Integer.parseInt(parts[0]);
						if (parts[0].length()==2) year = year + 2000; // will this lib life for 100 years ???
						int month = Integer.parseInt(parts[1])-1;
						int day   = Integer.parseInt(parts[2]);
						c.set(year,month, day);
				} else if (parts.length == 2) {
					c.set(Calendar.MONTH, Integer.parseInt(parts[0]) - 1);
					c.set(Calendar.DATE, Integer.parseInt(parts[1]));
				} else {
					parts = date.split("\\.");
					if (parts.length == 3) {
						int year = Integer.parseInt(parts[2]);
						if (parts[2].length()==2) year = year + 2000; // will this lib life for 100 years ???
						int month = Integer.parseInt(parts[1])-1;
						int day   = Integer.parseInt(parts[0]);
						c.set(year,month,day);
					} else if (parts.length == 2) {
						int year = Integer.parseInt(parts[1]);
						if (parts[1].length()==2) year = year + 2000; // will this lib life for 100 years ???
						int month = Integer.parseInt(parts[0])-1;					
						c.set(Calendar.MONTH, month);
						c.set(Calendar.YEAR, year);
					} else {
						parts = date.split("/");
						if (parts.length == 3)
							c.set(Integer.parseInt(parts[2]), Integer
											.parseInt(parts[0])-1, Integer
											.parseInt(parts[1]));
	
						if (parts.length == 2) {
							c.set(Calendar.MONTH, Integer.parseInt(parts[0]) - 1);
							c.set(Calendar.YEAR, Integer.parseInt(parts[1]));
						}
					}
				}
	
	//			if (zone != null) {
	//				TimeZone tz = TimeZone.getTimeZone(zone);
	//				c.setTimeZone(tz);
	//			}
				
				return c;
			} else
			if ( date.indexOf('.') > 0) {
				// german time 31.12.2000
				String[] parts = date.split("\\.");
				if (parts.length == 3) {
					int year = Integer.parseInt(parts[2]);
					if (parts[2].length()==2) year = year + 2000; // will this lib life for 100 years ???
					int month = Integer.parseInt(parts[1])-1;
					int day   = Integer.parseInt(parts[0]);
					c.set(year,month, day);
					return c;
				} else
					return null;
			} else
			if ( date.indexOf('/') > 0) {
				// france or UK 31/12/2000
				String[] parts = date.split("/");
				if (parts.length == 3) {
					int year = Integer.parseInt(parts[2]);
					if (parts[2].length()==2) year = year + 2000; // will this lib life for 100 years ???
					int month = Integer.parseInt(parts[1])-1;
					int day   = Integer.parseInt(parts[0]);
					c.set(year,month, day);
					return c;
				} else
					return null;
			}
			
			try {
				// timestamp
				long ts = Long.valueOf(date);
				c.setTimeInMillis(ts);
				return c;
			} catch (NumberFormatException e) {}
			
			return null;

		} catch (Throwable e) {
			log.t(in,e);
		}

		// return unknown - timestamp is 0
		return null;
	}

	private static int toint(String in, int def) {
		if (in == null) return def;
		try {
			return Integer.parseInt(in);
		} catch (NumberFormatException nfe) {
			return def;
		}
	}

}
