package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <p>ObjectToDate class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectToDate implements Caster<Object,Date>{

	/** {@inheritDoc} */
	@Override
	public Class<? extends Date> getToClass() {
		return Date.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	/** {@inheritDoc} */
	@Override
	public Date cast(Object in, Date def) {
		return cast(in, def, Locale.getDefault());
	}
	
	/**
	 * <p>cast.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @param def a {@link java.util.Date} object.
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link java.util.Date} object.
	 * @since 3.2.9
	 */
	public Date cast(Object in, Date def, Locale locale) {
		if (in == null) return def;
		if (in instanceof Date) return (Date)in;
		if (in instanceof Calendar) return ((Calendar)in).getTime();
		try {
			String ins = String.valueOf(in);
			Calendar c = ObjectToCalendar.toCalendar(ins, locale);
			if (c == null)
				return def;
			return c.getTime();
		} catch (Throwable t) {
			return def;
		}
	}

}
