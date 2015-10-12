import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.Scheduler;


public class TestScheduler {

	public static void main(String[] args) {

		Scheduler scheduler = new Scheduler();
		scheduler.start();
		
		scheduler.schedule(new CronJob("*/2 * * * *", new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("B Hello " + MDate.toIsoDateTime(new Date()) + " " + Thread.currentThread().getId());
			}
			
		}) {

			@Override
			public void doCaclulateNextExecution() {
				super.doCaclulateNextExecution();
				System.out.println("B --- " + MDate.toIsoDateTime(System.currentTimeMillis()) + " --> " + MDate.toIsoDateTime(getNextExecutionTime()));
			}

		});
		
		scheduler.schedule(new CronJob("*/4 * * * *", new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("A Hello " + MDate.toIsoDateTime(new Date()) + " " + Thread.currentThread().getId());
			}
			
		}) {
			
			@Override
			public void doCaclulateNextExecution() {
				super.doCaclulateNextExecution();
				System.out.println("A --- " + MDate.toIsoDateTime(System.currentTimeMillis()) + " --> " + MDate.toIsoDateTime(getNextExecutionTime()));
			}

		});
		
		while(true) {
			System.out.println( scheduler.getScheduledJobs() );
			MThread.sleep(1000 * 60); // 5 minutes
		}
	}

}
