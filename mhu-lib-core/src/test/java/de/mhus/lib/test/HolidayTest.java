package de.mhus.lib.test;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import de.mhus.lib.core.calendar.Holidays;
import de.mhus.lib.errors.NotFoundException;
import junit.framework.TestCase;

public class HolidayTest extends TestCase {

	@SuppressWarnings("deprecation")
	@Test
	public void testGermanHolidays() throws NotFoundException {
		Holidays holidays = Holidays.getHolidaysForLocale(Locale.GERMANY);
		assertNotNull(holidays);
		
		{
			Map<Date, String> h = holidays.getHolidays(null, 2018, "Bayern");
			System.out.println(h);
			// Karfreitag - good friday
			assertNotNull(h.get(new Date(2018-1900, 2, 30, 0, 0, 0)));
		}
		
	}

}
