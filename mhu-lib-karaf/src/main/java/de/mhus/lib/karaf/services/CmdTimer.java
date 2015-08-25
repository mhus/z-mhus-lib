package de.mhus.lib.karaf.services;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.schedule.OnceJob;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.SchedulerTimer;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.karaf.MOsgi;

@Command(scope = "mhus", name = "timer", description = "Default Timer Handling")
public class CmdTimer extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description="list,timeout,stacktrace,timeoutstacktrace", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute(CommandSession session) throws Exception {
		TimerFactory factory = MOsgi.getService(TimerFactory.class);
		SchedulerTimer scheduler = TimerFactoryImpl.getScheduler(factory);
		
		if (cmd.equals("list")) {
			List<SchedulerJob> scheduled = scheduler.getScheduledJobs();
			List<SchedulerJob> running = scheduler.getRunningJobs();
			
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Task","Job","Started","Stopped", "Description","Name","Scheduled","Timeout");
			
			for (SchedulerJob job : running) {
				table.addRowValues(job.getTask(),job,MDate.toIsoDateTime(job.getLastExecutionStart()),"Running",job.getDescription(),job.getName(),"Running",job.getTimeoutInMinutes());
			}
			for (SchedulerJob job : scheduled) {
				table.addRowValues(job.getTask(),job,MDate.toIsoDateTime(job.getLastExecutionStart()),MDate.toIsoDateTime(job.getLastExecutionStop()),job.getDescription(),job.getName(),MDate.toIsoDateTime(job.getScheduledTime()), job.getTimeoutInMinutes());
			}
			
			table.print(System.out);
		}
		if (cmd.equals("timeout")) {
			List<SchedulerJob> running = scheduler.getRunningJobs();
			
			ConsoleTable table = new ConsoleTable();
			table.setHeaderValues("Task","Job","Started","Stopped", "Description","Name","Scheduled","Timeout");
			
			long time = System.currentTimeMillis();
			for (SchedulerJob job : running) {
				long timeout = job.getTimeoutInMinutes() * MTimeInterval.MINUTE_IN_MILLISECOUNDS;
				if (timeout > 0 && timeout + job.getLastExecutionStart() <= time) {
					table.addRowValues(job.getTask(),job,MDate.toIsoDateTime(job.getLastExecutionStart()),"Running",job.getDescription(),job.getName(),"Running",job.getTimeoutInMinutes());
				}
			}
			
			table.print(System.out);
		}
		if (cmd.equals("stacktrace")) {
			List<SchedulerJob> running = scheduler.getRunningJobs();
			for (SchedulerJob job : running) {
				Thread thread = job.getThread();
				if (thread != null) {
					StackTraceElement[] stack = thread.getStackTrace();
					System.out.println( MCast.toString(job.getName() + " (threadId=" + thread.getId() + ")",stack) );
				}
			}
		}
		if (cmd.equals("timeoutstacktrace")) {
			List<SchedulerJob> running = scheduler.getRunningJobs();
			long time = System.currentTimeMillis();
			for (SchedulerJob job : running) {
				long timeout = job.getTimeoutInMinutes() * MTimeInterval.MINUTE_IN_MILLISECOUNDS;
				if (timeout > 0 && timeout + job.getLastExecutionStart() <= time) {
					Thread thread = job.getThread();
					if (thread != null) {
						StackTraceElement[] stack = thread.getStackTrace();
						System.out.println( MCast.toString(job.getName() + " (threadId=" + thread.getId() + ")",stack) );
					}
				}
			}
		}
		if (cmd.equals("dummy")) {
			scheduler.schedule(new OnceJob(System.currentTimeMillis() + MTimeInterval.MINUTE_IN_MILLISECOUNDS, new Observer() {
				
				@Override
				public void update(Observable o, Object arg) {
					log().i(">>> Start Dummy");
					MThread.sleep(MTimeInterval.MINUTE_IN_MILLISECOUNDS * 2);
					log().i("<<< Stop Dummy");
				}
			})
			{
				{
					setTimeoutInMinutes(1);
				}
				
				@Override
				public void doTimeoutReached() {
					log().i("+++ Dummy Timeout Reached");
				}
			}
			);
		}
		return null;
	}

}
