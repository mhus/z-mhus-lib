package de.mhus.lib.test;

import de.mhus.lib.core.MTimeInterval;
import junit.framework.TestCase;

public class MTimeIntervalTest extends TestCase {

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
	}
	
	public void testGet() {
		
		MTimeInterval i = new MTimeInterval("1d 1h 5M");
		assertEquals(1, i.getAllDays());
		
		assertEquals(25, i.getAllHours());
		
		assertEquals(25 * 60 + 5, i.getAllMinutes());
	}
	
}
