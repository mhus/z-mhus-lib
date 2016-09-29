package de.mhus.lib.core.jmx;

import java.util.Calendar;
import java.util.Date;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MDate;

/**
 * <p>JmxDate class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@JmxManaged(descrition = "Date Value")
public class JmxDate extends MJmx {

	private Calendar value = Calendar.getInstance();

	/**
	 * <p>Constructor for JmxDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public JmxDate(String name) {
		super(true,name);
	}
	
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.util.Date} object.
	 * @return a {@link de.mhus.lib.core.jmx.JmxDate} object.
	 */
	public JmxDate setValue(Date  value) {
		this.value.setTime(value);
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.util.Date} object.
	 */
	@JmxManaged
	public Date getValue() {
		return value.getTime();
	}

	
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MDate.toIsoDateTime(value);
	}

}
