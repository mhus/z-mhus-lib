package de.mhus.lib.core.base.service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(HolidayProviderImpl.class)
public interface HolidayProviderIfc {

	/**
	 * Returns if the given day is a holiday in the given area.
	 * 
	 * @param locale The requested area or null for the default area.
	 * @param date The day to check
	 * @return true if the day (ignoring time) is a holiday.
	 */
	boolean isHoliday(Locale locale, Date date);

	/**
	 * Returns true if the day is not a Sunday and not a holiday.
	 * @param locale 
	 * 
	 * @param date
	 * @return
	 */
	boolean isWorkingDay(Locale locale, Date date);
	
	/**
	 * Returns a list of holidays for the area and year.
	 * 
	 * @param locale
	 * @param year
	 * @return
	 */
	Map<Date, String> getHolidays(Locale locale, int year);
	
}
