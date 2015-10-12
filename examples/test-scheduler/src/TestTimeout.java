import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.schedule.CronJob;
import de.mhus.lib.core.schedule.Scheduler;


public class TestTimeout {

	public static void main(String[] args) {

		Scheduler scheduler = new Scheduler();
		scheduler.start();
		
		CronJob job = new CronJob("* * * * *", new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				System.out.println(">>> Start");
				MThread.sleep(MTimeInterval.HOUR_IN_MILLISECOUNDS);
				System.out.println("<<< Stop");
			}

		}) {

			@Override
			public void doTimeoutReached() {
				System.out.println("--- Timeout Reached " + MDate.toIsoDateTime(System.currentTimeMillis()) );
			}
			
			
		};
		
		job.setTimeoutInMinutes(1);
		
		scheduler.schedule(job);
		
		while(true) {
			System.out.println( scheduler.getScheduledJobs() );
			MThread.sleep(1000 * 60); // 5 minutes
		}

	}

}
