

Usage of CronSheduler:
``` java
public class TestScheduler {

	public static void main(String[] args) {

		
		// operation itself
		MTimerTask task = new MTimerTask() {

			@Override
			public void doit() throws Exception {
				System.out.println("Executed: " + MDate.toIsoDateTime(System.currentTimeMillis()));
			}
			
		};
		
		
		// schedule filter
		SchedulerJob s = new CronJob("0,15,30,45 * * * *", task);
		
		// timer
		Timer t = new Timer();
		t.schedule(s, 10000, 10000);
	
		
		
	}

}
```
