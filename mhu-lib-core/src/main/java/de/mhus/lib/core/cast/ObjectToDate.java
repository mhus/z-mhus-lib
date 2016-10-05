package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObjectToDate implements Caster<Object,Date>{

	@Override
	public Class<? extends Date> getToClass() {
		return Date.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public Date cast(Object in, Date def) {
		return cast(in, def, Locale.getDefault());
	}
	
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
