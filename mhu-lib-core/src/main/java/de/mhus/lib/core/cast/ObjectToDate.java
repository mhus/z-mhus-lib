package de.mhus.lib.core.cast;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.mhus.lib.core.MCast;

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
		if (in == null) return def;
		if (in instanceof Date) return (Date)in;
		if (in instanceof Calendar) return ((Calendar)in).getTime();
		try {
			String ins = String.valueOf(in);
			Calendar c = ObjectToCalendar.toCalendar(ins);
			if (c == null)
				return def;
			return c.getTime();
		} catch (Throwable t) {
			return def;
		}
	}

}
