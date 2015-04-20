package java.util;

public class TimerHack {

	public static boolean isCancelled(TimerTask task) {
		return task.state == TimerTask.CANCELLED;
	}
	
}
