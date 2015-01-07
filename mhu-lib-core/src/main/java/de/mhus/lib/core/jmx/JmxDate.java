package de.mhus.lib.core.jmx;

import java.util.Calendar;
import java.util.Date;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MDate;

@JmxManaged(descrition = "Date Value")
public class JmxDate extends MJmx {

	private Calendar value = Calendar.getInstance();

	public JmxDate(String name) {
		super(true,name);
	}
	
	public JmxDate setValue(Date  value) {
		this.value.setTime(value);
		return this;
	}
	
	@JmxManaged
	public Date getValue() {
		return value.getTime();
	}

	
	
	public String toString() {
		return MDate.toIsoDateTime(value);
	}

}
