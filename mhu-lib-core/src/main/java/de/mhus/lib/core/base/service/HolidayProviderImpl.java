package de.mhus.lib.core.base.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;

public class HolidayProviderImpl extends MLog implements HolidayProviderIfc {
	
	private HashMap<String, Map<Date, String>> days = new HashMap<>();
	
	@Override
	public boolean isHoliday(Locale locale, Date date) {
		@SuppressWarnings("deprecation")
		Map<Date, String> map = getHolidays(locale, date.getYear() + 1900);
		if (map == null) return false;
		return map.containsKey(MDate.toDateOnly(date));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isWorkingDay(Locale locale, Date date) {
		return date.getDay() != 0 && !isHoliday(locale, date);
	}

	@Override
	public synchronized Map<Date, String> getHolidays(Locale locale, int year) {
		if (locale == null) locale = Locale.getDefault();
		Map<Date, String> map = days.get(locale + "_" + year);
		if (map == null) {
			// load map file
			File f = null;
			f = MApi.getFile("holidays_" + locale.toString() + "_" + year + ".txt");
			if (f == null || !f.exists())
				f = MApi.getFile("holidays_" + year + ".txt");
			if (f != null && f.exists()) {
				try {
					map = new HashMap<>();
					for ( String line : MFile.readLines(f, true)) {
						line = line.trim();
						if (MString.isSet(line) && !line.startsWith("#")) {
							String date = null;
							String msg = "";
							if (MString.isIndex(line, ' ')) {
								date = MString.beforeIndex(line, ' ').trim();
								msg = MString.afterIndex(line, ' ').trim();
							} else {
								date = line;
							}
							Date d = MDate.toDate(date, null);
							if (d != null) {
								d = MDate.toDateOnly(d);
								map.put(d, msg);
							}
						}
					}
					days.put(locale + "_" + year,map);
				} catch (Exception e) {
					log().d(e);
				}
			} else {
				log().d("Definition file not found",locale,year);
				map = new HashMap<>();
				days.put(locale + "_" + year,map);
			}
		}
		return map;
	}

}
