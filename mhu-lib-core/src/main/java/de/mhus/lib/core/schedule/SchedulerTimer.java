package de.mhus.lib.core.schedule;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class SchedulerTimer {

	private Timer timer;
	private LinkedList<Scheduler> queue = new LinkedList<>();
	
	public void start() {
		if (timer != null) return;
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				doTick();
			}
		}, 1000, 1000);
	}
	
	protected void doTick() {
		while (true) {
			synchronized (queue) {
				Scheduler first = queue.getFirst();
				if (first.getNextExecutionTime() < System.currentTimeMillis()) {
					queue.removeFirst();
				} else {
					first = null;
				}
			}
		}
	}

	public void stop() {
		if (timer == null) return;
		timer.cancel();
		timer = null;
	}
	
	public void schedule(Scheduler scheduler) {
		synchronized (queue) {
			queue.add(0, scheduler);
		}
	}
	
}
