package de.mhus.lib.core.schedule;

import java.util.Calendar;
import java.util.Observer;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MTimeInterval;

/**
 * Schedule tasks like Crontab (man -S 5 crontab). Next scheduling is done after execution of a task. Example * * * * * every minute,
 *          field         allowed values
 *          -----         --------------
 *          minute        0-59
 *          hour          0-23
 *          day of month  1-31
 *          month         1-12 (or names, see below)
 *          day of week   1-7 (1 is Sunday)
 *          
 * @author mikehummel
 *
 */
public class CronJob extends SchedulerJob {

	private Definition definition;
	private boolean restrictive = true; // if not executed in the minute of scheduled time, a new time is scheduled


	public CronJob(String name, Definition definition, boolean restrictive, Observer task) {
		super(name, task);
		setRestrictive(restrictive);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = definition;
	}
	
	public CronJob(String minute, String hour, String dayOfMonth, String month, String dayOfWeek, Observer task) {
		super(task);
		if (minute == null) minute = "*";
		if (hour == null) hour = "*";
		if (dayOfMonth == null) dayOfMonth = "*";
		if (month == null) month = "*";
		if (dayOfWeek == null) dayOfWeek = "*";
		this.definition = new Definition(minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek);
	}
	
	public CronJob(String definition, Observer task) {
		super(task);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = new Definition(definition);
	}

	public CronJob(String name, String definition, Observer task) {
		super(name, task);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = new Definition(definition);
	}
	
	public CronJob(String name, String definition, boolean restrictive, Observer task) {
		super(name, task);
		setRestrictive(restrictive);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = new Definition(definition);
	}
	
	@Override
	public void doCaclulateNextExecution() {
		nextExecutionTime = definition.calculateNext( System.currentTimeMillis() );
	}

	@Override
	protected boolean isExecutionTimeReached() {
		if (restrictive) {
			if (nextExecutionTime > 0 && System.currentTimeMillis() + MTimeInterval.MINUTE_IN_MILLISECOUNDS <= nextExecutionTime) {
				log.d("cron restrictive over time, reschedule job",getName(),getTask());
				doCaclulateNextExecution();
			}
		}
			
		return super.isExecutionTimeReached();
		
	}

	public boolean isRestrictive() {
		return restrictive;
	}

	public void setRestrictive(boolean restrictive) {
		this.restrictive = restrictive;
	}


	public static class Definition {

		private int[] allowedMinutes;
		private int[] allowedHours;
		private int[] allowedDaysMonth;
		private int[] allowedMonthes;
		private int[] allowedDaysWeek;

		public Definition() {
		}
		
		public Definition(String definition) {
			String[] parts = definition.split(" ");
			allowedMinutes = MCast.toIntIntervalValues(parts[0], 0, 59);
			allowedHours = MCast.toIntIntervalValues(parts[1], 0, 23);
			allowedDaysMonth = MCast.toIntIntervalValues(parts[2], 1, 31);
			allowedMonthes = MCast.toIntIntervalValues(parts[3], 0, 11);
			allowedDaysWeek = MCast.toIntIntervalValues(parts[4], 1, 7);
		}
		
		public long calculateNext(long start) {
			
			Calendar next = Calendar.getInstance();
			
			// obligatory next minute
			next.set(Calendar.MILLISECOND, 0);
			next.set(Calendar.SECOND, 0);
			next.add(Calendar.MINUTE, 1);
			
			if (allowedMinutes != null) {
				int[] d = findNextAllowed( allowedMinutes, next.get(Calendar.MINUTE) );
				next.set(Calendar.MINUTE, d[1]);
				if (d[2] == 1)
					next.add(Calendar.HOUR, 1);
			}
			
			if (allowedHours != null) {
				int[] d = findNextAllowed( allowedHours, next.get(Calendar.HOUR) );
				next.set(Calendar.HOUR, d[1]);
				if (d[2] == 1)
					next.add(Calendar.DATE, 1);
			}
			if (allowedDaysMonth != null) {
				int[] d = findNextAllowed( allowedDaysMonth, next.get(Calendar.DAY_OF_MONTH) );
				next.set(Calendar.DAY_OF_MONTH, d[1]);
				if (d[2] == 1)
					next.add(Calendar.MONTH, 1);
			}
			if (allowedMonthes != null) {
				int[] d = findNextAllowed( allowedMonthes, next.get(Calendar.MONTH) );
				next.set(Calendar.MONTH, d[1]);
				if (d[2] == 1)
					next.add(Calendar.YEAR, 1);
			}
			if (allowedDaysWeek != null) {
				int[] d = findNextAllowed( allowedDaysWeek, next.get(Calendar.DAY_OF_WEEK) );
				next.set(Calendar.DAY_OF_WEEK, d[1]);
				if (d[2] == 1)
					next.add(Calendar.WEEK_OF_YEAR, 1);
			}
			return next.getTimeInMillis();
		}

		private int[] findNextAllowed(int[] allowed, int current) {
			int i = 0;
			if (allowed == null || allowed.length == 0) return new int[]{i,current, 0};
			for (int a : allowed) {
				if (a >= current) return new int[]{i,a, 0};
				i++;
			}
			return new int[] {0,allowed[0], 1};
		}
		
	}
}
