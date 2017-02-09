package de.mhus.lib.karaf.services;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.base.service.TimerFactory;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.schedule.MutableSchedulerJob;
import de.mhus.lib.core.schedule.OnceJob;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.schedule.SchedulerTimer;
import de.mhus.lib.karaf.MOsgi;

@Command(scope = "mhus", name = "timer", description = "Default Timer Handling")
@Service
public class CmdTimer extends MLog implements Action {

	@Argument(index=0, name="cmd", required=true, description=
			"Command to execute"
			+ " list,\n"
			+ " timeout,\n"
			+ " stacktrace,\n"
			+ " timeoutstacktrace,\n"
//			+ " schedule <name> <time>,\n"
			+ " done <name> <done>,\n"
			+ " disable/enable/cancel/remove <name>,\n"
			+ " run <name>,\n"
			+ " configure <name> <configuration>\n"
			+ "  name is allways a pattern use * to define placeholders", multiValued=false)
    String cmd;

	@Argument(index=1, name="paramteters", required=false, description="Parameters", multiValued=true)
    String[] parameters;

	@Override
	public Object execute() throws Exception {
		TimerFactory factory = MOsgi.getService(TimerFactory.class);
		SchedulerTimer scheduler = TimerFactoryImpl.getScheduler(factory);
		
		if (cmd.equals("list")) {
			List<SchedulerJob> scheduled = scheduler.getScheduledJobs();
			List<SchedulerJob> running = scheduler.getRunningJobs();
			
			ConsoleTable table = new ConsoleTable();
			table.setLineSpacer(true);
			table.setHeaderValues(
					"Name",
					"Task",
					"Job",
					"Started",
					"Stopped", 
					"Scheduled/Thread",
					"Timeout",
					"Canceled",
					"Done",
					"Status"
				);
			
			for (SchedulerJob job : running) {
				table.addRowValues(
						job.getName(),
						job.getTask(),
						job,
						MDate.toIsoDateTime(job.getLastExecutionStart()),
						"Running",
						job.getThread(),
						job.getTimeoutInMinutes(),
						job.isCanceled(),
						job.isDone(),
						getStatus(job)
					);
			}
			for (SchedulerJob job : scheduled) {
				table.addRowValues(
						job.getName(),
						job.getTask(),
						job,
						MDate.toIsoDateTime(job.getLastExecutionStart()),
						MDate.toIsoDateTime(job.getLastExecutionStop()),
						MDate.toIsoDateTime(job.getScheduledTime()), 
						job.getTimeoutInMinutes(),
						job.isCanceled(),
						job.isDone(),
						getStatus(job)
					);
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
		if (cmd.equals("schedule")) {
			Date time = MCast.toDate(parameters[1], null);
			if (time == null) {
				System.out.println("Malformed time");
				return null;
			}
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null && job instanceof MutableSchedulerJob) {
					((MutableSchedulerJob)job).setScheduledTime(time.getTime());

					System.out.println("OK, Scheduled " + job.getName() +" to " + MDate.toIsoDateTime( time ) );
				}
			}
		}
		if (cmd.equals("done")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null && job instanceof MutableSchedulerJob) {
					((MutableSchedulerJob)job).setDone( MCast.toboolean(parameters[1], false));
					
					System.out.println("OK " + job.getName());
				}
			}
		}
		if (cmd.equals("remove")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null) {
					scheduler.getQueue().removeJob(job);
					System.out.println("OK " + job.getName());
				}
			}
		}
		if (cmd.equals("disable")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null && job instanceof MutableSchedulerJob) {
					((MutableSchedulerJob)job).doReschedule(scheduler, SchedulerJob.DISABLED_TIME);
					System.out.println("OK " + job.getName());
				}
			}
		}
		if (cmd.equals("enable")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null && job instanceof MutableSchedulerJob) {
					((MutableSchedulerJob)job).doReschedule(scheduler, SchedulerJob.CALCULATE_NEXT);
					System.out.println("OK " + job.getName());
				}
			}
		}
		if (cmd.equals("cancel")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null) {
					job.cancel();
					System.out.println("OK " + job.getName());
				}
			}
		}
		if (cmd.equals("configure")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null) {
					if (job instanceof MutableSchedulerJob) {
						boolean ret = ((MutableSchedulerJob)job).doReconfigure(parameters[1]);
						System.out.println("OK " + job.getName() + " " + ret);
						if (ret)
							((MutableSchedulerJob) job).doReschedule(scheduler, SchedulerJob.CALCULATE_NEXT);
					}
				}
			}
		}
		if (cmd.equals("run")) {
			for (SchedulerJob job : getScheduledJob(scheduler, parameters[0]) ) {
				if (job != null) {
					scheduler.doExecuteJob(job, true);
				}
			}
		}
		if (cmd.equals("check")) {
			TimerFactoryImpl.doCheckTimers();
		}
		if (cmd.equals("debug")) {
			TimerFactoryImpl.doDebugInfo();
		}
		return null;
	}

	private String getStatus(SchedulerJob job) {
		long t = job.getNextExecutionTime();
		if (t == SchedulerJob.CALCULATE_NEXT) return "Calculate";
		if (t == SchedulerJob.DISABLED_TIME) return "Disabled";
		if (t == SchedulerJob.REMOVE_TIME) return "Remove";
		return "OK";
	}

	private List<SchedulerJob> getScheduledJob(SchedulerTimer scheduler, String jobId) {
		List<SchedulerJob> jobs = scheduler.getScheduledJobs();
		LinkedList<SchedulerJob> out = new LinkedList<>();
		for (SchedulerJob job : jobs) {
			if (MString.compareFsLikePattern(job.getName(),jobId)) out.add(job);
		}
		return out;
	}

}
