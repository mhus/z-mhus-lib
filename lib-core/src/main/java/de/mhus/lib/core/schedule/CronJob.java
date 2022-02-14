/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.schedule;

import java.util.Calendar;

import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.service.HolidayProviderIfc;

/**
 * Schedule tasks like Crontab (man -S 5 crontab). Next scheduling is done after execution of a
 * task. Example * * * * * every minute, field allowed values ----- -------------- minute 0-59 hour
 * 0-23 day of month 1-31 month 1-12 (or names, see below) day of week 1-7 (1 is Sunday) specials w
 * = only working days disabled = disabled by default
 *
 * @author mikehummel
 */
public class CronJob extends SchedulerJob implements MutableSchedulerJob {

    private Definition definition;
    private boolean restrictive =
            true; // if not executed in the minute of scheduled time, a new time is scheduled

    public CronJob(String name, Definition definition, boolean restrictive, ITimerTask task) {
        super(name, task);
        setRestrictive(restrictive);
        if (definition == null) throw new NullPointerException("definition is null");
        this.definition = definition;
    }

    public CronJob(
            String minute,
            String hour,
            String dayOfMonth,
            String month,
            String dayOfWeek,
            ITimerTask task) {
        super(task);
        if (minute == null) minute = "*";
        if (hour == null) hour = "*";
        if (dayOfMonth == null) dayOfMonth = "*";
        if (month == null) month = "*";
        if (dayOfWeek == null) dayOfWeek = "*";
        this.definition =
                new Definition(
                        minute + " " + hour + " " + dayOfMonth + " " + month + " " + dayOfWeek);
    }

    public CronJob(String definition, ITimerTask task) {
        super(task);
        if (definition == null) throw new NullPointerException("definition is null");
        this.definition = new Definition(definition);
    }

    public CronJob(String name, String definition, ITimerTask task) {
        super(name, task);
        if (definition == null) throw new NullPointerException("definition is null");
        this.definition = new Definition(definition);
    }

    public CronJob(String name, String definition, boolean restrictive, ITimerTask task) {
        super(name, task);
        setRestrictive(restrictive);
        if (definition == null) throw new NullPointerException("definition is null");
        this.definition = new Definition(definition);
    }

    @Override
    public void doCaclulateNextExecution() {
        setNextExecutionTime(definition.calculateNext(System.currentTimeMillis()));
    }

    @Override
    protected boolean isExecutionTimeReached() {
        if (restrictive) {
            if (getNextExecutionTime() > 0
                    && System.currentTimeMillis() + MPeriod.MINUTE_IN_MILLISECONDS
                            <= getNextExecutionTime()) {
                log.d("cron restrictive over time, reschedule job", getName(), getTask());
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
        private String definition;
        private boolean disabled = false;
        private boolean onlyWorkingDays = false;

        public Definition() {}

        public Definition(String definition) {
            this.definition = definition.trim();
            String[] parts = this.definition.split(" ");
            if (parts.length == 0) {
                parts = new String[] {"disabled"};
            }
            if (parts.length == 1) {
                if (parts[0].equals("disabled"))
                    parts = new String[] {"*", "*", "*", "*", "*", "disabled"};
                else {
                    int i = MCast.toint(parts[0], 0);
                    if (i > 0) {
                        int m = 60 / i % 60;
                        int h = 24 / (i / 60) % 24;
                        parts =
                                new String[] {
                                    m > 0 ? "*/" + m : "*", h > 0 ? "*/" + h : "*", "*", "*", "*"
                                };
                    }
                }
            }

            if (parts.length > 0) allowedMinutes = MCast.toIntIntervalValues(parts[0], 0, 59);

            if (parts.length > 1) allowedHours = MCast.toIntIntervalValues(parts[1], 0, 23);

            if (parts.length > 2) allowedDaysMonth = MCast.toIntIntervalValues(parts[2], 1, 31);

            if (parts.length > 3) allowedMonthes = MCast.toIntIntervalValues(parts[3], 0, 11);

            if (parts.length > 4) {
                parts[4] = parts[4].toLowerCase();
                parts[4] = parts[4].replace("so", "1");
                parts[4] = parts[4].replace("mo", "2");
                parts[4] = parts[4].replace("tu", "3");
                parts[4] = parts[4].replace("we", "4");
                parts[4] = parts[4].replace("th", "5");
                parts[4] = parts[4].replace("fr", "6");
                parts[4] = parts[4].replace("sa", "7");
                allowedDaysWeek = MCast.toIntIntervalValues(parts[4], 1, 7);
            }

            if (parts.length > 5) {
                if (parts[5].indexOf("disabled") >= 0) disabled = true;
                if (parts[5].indexOf("w") >= 0) onlyWorkingDays = true;
            }
        }

        public long calculateNext(long start) {

            if (disabled) return DISABLED_TIME;

            Calendar next = Calendar.getInstance();
            if (start > 0) next.setTimeInMillis(start);

            // obligatory next minute
            next.set(Calendar.MILLISECOND, 0);
            next.set(Calendar.SECOND, 0);
            next.add(Calendar.MINUTE, 1);

            boolean retry = true;
            int retryCnt = 0;
            while (retry && retryCnt < 10) {
                retry = false;
                retryCnt++;

                if (allowedMinutes != null) {
                    int[] d = findNextAllowed(allowedMinutes, next.get(Calendar.MINUTE));
                    next.set(Calendar.MINUTE, d[1]);
                    if (d[2] == 1) next.add(Calendar.HOUR_OF_DAY, 1);
                }
                if (allowedHours != null) {
                    int[] d = findNextAllowed(allowedHours, next.get(Calendar.HOUR_OF_DAY));
                    next.set(Calendar.HOUR_OF_DAY, d[1]);
                    if (d[2] == 1) next.add(Calendar.DATE, 1);
                }
                if (allowedDaysMonth != null) {
                    int[] d = findNextAllowed(allowedDaysMonth, next.get(Calendar.DAY_OF_MONTH));
                    next.set(Calendar.DAY_OF_MONTH, d[1]);
                    if (d[2] == 1) next.add(Calendar.MONTH, 1);
                }
                if (allowedMonthes != null) {
                    int[] d = findNextAllowed(allowedMonthes, next.get(Calendar.MONTH));
                    next.set(Calendar.MONTH, d[1]);
                    if (d[2] == 1) next.add(Calendar.YEAR, 1);
                }
                if (allowedDaysWeek != null) {
                    int[] d = findNextAllowed(allowedDaysWeek, next.get(Calendar.DAY_OF_WEEK));
                    if (next.get(Calendar.DAY_OF_WEEK) != d[1]) {
                        next.set(Calendar.DAY_OF_WEEK, d[1]);
                        next.set(Calendar.HOUR, 0);
                        next.set(Calendar.MINUTE, 0);
                        retry = true;
                    }
                    if (d[2] == 1) next.add(Calendar.WEEK_OF_YEAR, 1);
                }

                if (onlyWorkingDays) {
                    HolidayProviderIfc holidayProvider = M.l(HolidayProviderIfc.class);
                    if (holidayProvider != null) {
                        while (!holidayProvider.isWorkingDay(null, next.getTime())) {
                            next.add(Calendar.DAY_OF_MONTH, 1);
                            next.set(Calendar.HOUR, 0);
                            next.set(Calendar.MINUTE, 0);
                            retry = true;
                        }
                    }
                }
            }
            return next.getTimeInMillis();
        }

        private int[] findNextAllowed(int[] allowed, int current) {
            int i = 0;
            if (allowed == null || allowed.length == 0) return new int[] {i, current, 0};
            for (int a : allowed) {
                if (a >= current) return new int[] {i, a, 0};
                i++;
            }
            return new int[] {0, allowed[0], 1};
        }

        @Override
        public String toString() {
            return definition;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }

    @Override
    public String toString() {
        return CronJob.class.getSimpleName() + "," + definition;
    }

    @Override
    public void doReschedule(Scheduler queue, long time) {
        super.doReschedule(queue, time);
    }

    @Override
    public void setDone(boolean done) {
        super.setDone(done);
    }

    @Override
    public boolean doReconfigure(String config) {
        this.definition = new Definition(config);
        return true;
    }

    @Override
    public void setScheduledTime(long scheduledTime) {
        super.setScheduledTime(scheduledTime);
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }
}
