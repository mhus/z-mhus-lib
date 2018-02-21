package de.mhus.lib.test;

import java.util.Date;

import org.junit.Test;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.CronJob.Definition;
import junit.framework.TestCase;

public class SchedulerTest extends TestCase {

	@Test
	public void testCron() {
		{
			Definition def = new CronJob.Definition("* * * * *");
			Date start = MDate.toDate("19-07-07 10:10:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("19-07-07 10:11:00", null), next);
		}
		{
			Definition def = new CronJob.Definition("15,30,45,0 * * * *");
			Date start = MDate.toDate("19-07-07 10:10:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("19-07-07 10:15:00", null), next);
		}
		{
			Definition def = new CronJob.Definition("15-30 * * * *");
			Date start = MDate.toDate("19-07-07 10:10:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("19-07-07 10:15:00", null), next);
		}
		{
			Definition def = new CronJob.Definition("15-30/5 * * * *");
			Date start = MDate.toDate("19-07-07 10:16:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("19-07-07 10:20:00", null), next);
		}
		{
			Definition def = new CronJob.Definition("* * * * 2-6");
			Date start = MDate.toDate("2018-02-24 10:10:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("2018-02-26 0:00:00", null), next);
		}
		{
			Definition def = new CronJob.Definition("* * * * * w");
			Date start = MDate.toDate("2018-01-01 10:10:10", null);
			Date next = new Date( def.calculateNext(start.getTime()) );
			assertEquals(MDate.toDate("2018-01-02 0:00:00", null), next);
		}
	}
	
}
