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
	
	private HashMap<String, Map<String, String>> days = new HashMap<>();
	
	@Override
	public boolean isHoliday(Locale locale, Date date) {
		@SuppressWarnings("deprecation")
		Map<String, String> map = getHolidays(locale, date.getYear() + 1900);
		if (map == null) return false;
		return map.containsKey(date.getDate() + "." + (date.getMonth()+1));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isWorkingDay(Locale locale, Date date) {
		return date.getDay() != 0 && !isHoliday(locale, date);
	}

	@Override
	public synchronized Map<String, String> getHolidays(Locale locale, int year) {
		if (locale == null) locale = Locale.getDefault();
		Map<String, String> map = days.get(locale + "_" + year);
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
								map.put(d.getDate() + "." + (d.getMonth()+1), msg);
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
				fillDefaultHolydays(locale, map);
				days.put(locale + "_" + year,map);
			}
		}
		return map;
	}

	protected void fillDefaultHolydays(Locale locale, Map<String, String> map) {
		map.put("1.1", "New Year");
		map.put("1.5","1. May");
		map.put("25.12","X-Mas");
		map.put("26.12","X-Mas");
		// TODO provide a extensive list
	}

}
