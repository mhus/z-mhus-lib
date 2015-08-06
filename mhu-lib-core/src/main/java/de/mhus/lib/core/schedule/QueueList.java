package de.mhus.lib.core.schedule;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MTimeInterval;

public class QueueList implements SchedulerQueue {

	private LinkedList<SchedulerJob> list = new LinkedList<>();
	
	@Override
	public List<SchedulerJob> removeJobs(long toTime) {
		LinkedList<SchedulerJob> out = null;
		synchronized (this) {
			while (true) {
				if (list.size() == 0) break;
				SchedulerJob first = list.getFirst();
				if (first.getScheduledTime() <= toTime) {
					if (out == null) out = new LinkedList<>();
					out.add(first);
					list.removeFirst();
				} else {
					break;
				}
			}
		}
		return out;
	}

	@Override
	public void doSchedule(SchedulerJob schedulerJob) {
		long time = schedulerJob.getScheduledTime();
		if (time <= 0) return;
		synchronized (this) {
			Iterator<SchedulerJob> iter = list.iterator();
			int pos = 0;
			while (iter.hasNext()) {
				SchedulerJob item = iter.next();
				if (item.getNextExecutionTime() >= time) {
					list.add(pos, schedulerJob);
					return;
				}
				pos++;
			}
			list.add(schedulerJob);
		}
	}

}
