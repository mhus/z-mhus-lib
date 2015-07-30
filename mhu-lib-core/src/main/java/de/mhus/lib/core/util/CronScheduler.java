package de.mhus.lib.core.util;

import java.util.Calendar;
import java.util.Observer;

import de.mhus.lib.core.MCast;

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
public class CronScheduler extends Scheduler {

	private Definition definition;


	public CronScheduler(Definition definition, Observer task) {
		super(task);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = definition;
	}
	
	public CronScheduler(String definition, Observer task) {
		super(task);
		if (definition == null) throw new NullPointerException("definition is null");
		this.definition = new Definition(definition);
	}

	@Override
	public void doCaclulateNextExecution() {
		nextExecutionTime = definition.calculateNext( System.currentTimeMillis() );
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
