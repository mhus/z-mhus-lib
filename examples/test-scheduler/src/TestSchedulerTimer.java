import java.util.Timer;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.SchedulerJob;


public class TestSchedulerTimer {

	public static void main(String[] args) {

		
		// operation itself
		MTimerTask task = new MTimerTask() {

			@Override
			public void doit() throws Exception {
				System.out.println("Executed: " + MDate.toIsoDateTime(System.currentTimeMillis()));
				MThread.sleep(5000);
			}
			
		};
		
		
		// schedule filter
		SchedulerJob s1 = new CronJob("*/1 * * * *", task) {
			@Override
			public void doCaclulateNextExecution() {
				super.doCaclulateNextExecution();
				System.out.println("A --- " + MDate.toIsoDateTime(System.currentTimeMillis()) + " --> " + MDate.toIsoDateTime(getNextExecutionTime()));
			}

		};

		// schedule filter
		SchedulerJob s2 = new CronJob("*/2 * * * *", task) {
			@Override
			public void doCaclulateNextExecution() {
				super.doCaclulateNextExecution();
				System.out.println("B --- " + MDate.toIsoDateTime(System.currentTimeMillis()) + " --> " + MDate.toIsoDateTime(getNextExecutionTime()));
			}

		};
		
		// timer
		Timer t = new Timer();
		t.schedule(s1, 1, 10000);
		t.schedule(s2, 1, 10000);
	
		
		
	}

}
