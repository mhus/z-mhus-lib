package de.mhus.lib.test;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MTimeInterval;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class MTimeIntervalTest {

	@Test
	public void testAverageMonth() {
		
		int period = 10000; // years
		
		Date from = MCast.toDate("2000-01-01T00:00:00", null);
		Date to = MDate.toDate((2000 + period) + "-01-01T00:00:00", null);
		
		long diff = to.getTime() - from.getTime();
		
		long average = diff / (period * 12);
		System.out.println("Average month ms for "+period+" years is " + average);
		{
			MTimeInterval interval = new MTimeInterval(average);
			System.out.println("Manual  average month days is " + (average / 1000 / 60 / 60 / 24));
			System.out.println("AllDays average month days is " + interval.getAllDays());
			assertEquals(30, interval.getAllDays());
		}
		{
			MTimeInterval interval = new MTimeInterval("1y");
			assertEquals(1, interval.getAverageYears());
			assertEquals(12, interval.getAverageMonths());
			System.out.println("1 year days is " + interval.getAllDays());
		}
		{
			// days of 10.000 years
			MTimeInterval interval = new MTimeInterval(period + "y");
			float days = (diff / (1000*60*60*24));
			assertEquals(days, interval.getAllDays());
		}
		
	}
	
	@Test
	public void testParse() {
		{
			MTimeInterval i = new MTimeInterval("1h");
			assertEquals(MTimeInterval.HOUR_IN_MILLISECOUNDS, i.getAllMilliseconds());
		}
		{
			MTimeInterval i = new MTimeInterval("1M");
			assertEquals(MTimeInterval.MINUTE_IN_MILLISECOUNDS, i.getAllMilliseconds());
		}
		{
			MTimeInterval i = new MTimeInterval("1d");
			assertEquals(MTimeInterval.DAY_IN_MILLISECOUNDS, i.getAllMilliseconds());
		}
		{
			MTimeInterval i = new MTimeInterval("1d 1h");
			assertEquals(1, i.getAllDays());
		}
		{
			MTimeInterval i = new MTimeInterval("1d 24h");
			assertEquals(2, i.getAllDays());
		}
		{
			MTimeInterval i = new MTimeInterval("1d 25h");
			assertEquals(2, i.getAllDays());
		}
	}
	
	@Test
	public void testGet() {
		
		MTimeInterval i = new MTimeInterval("1d 1h 5M");
		assertEquals(1, i.getAllDays());
		
		assertEquals(25, i.getAllHours());
		
		assertEquals(25 * 60 + 5, i.getAllMinutes());
	}
	{
		MTimeInterval i = new MTimeInterval(2629746000l);
		assertEquals(2629746000l, i.getAllMilliseconds());
		assertEquals(2629746, i.getAllSecounds());
		assertEquals(2629746 / 60, i.getAllMinutes());
		assertEquals(2629746 / 60 / 60, i.getAllHours());
		assertEquals(2629746 / 60 / 60 / 24, i.getAllDays());
	}
	
}
