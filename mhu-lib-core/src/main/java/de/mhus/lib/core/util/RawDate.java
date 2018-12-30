package de.mhus.lib.core.util;

public class RawDate {

	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public int getMillies() {
		return (int)(time % 1000);
	}
	
	public int getSecond() {
		return (int)(time / 1000 % 60);
	}

	public int getMinute() {
		return (int)(time / 1000 / 60 % 60);
	}

	public int getHour() {
		return (int)(time / 1000 / 60 / 60 % 24);
	}

	public long getDaysSince1970() {
		return time / 1000 / 60 / 60 / 24;
	}

}
