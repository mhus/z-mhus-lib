package de.mhus.lib.core.cast;

import java.sql.Date;
import java.util.UUID;

import de.mhus.lib.core.MCast;

public class ObjectToSqlDate implements Caster<Object,Date>{

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
		try {
			String ins = MCast.toString(in);
			return MCast.toSqlDate( MCast.toDate(ins, def) );
		} catch (Throwable t) {
			return def;
		}
	}

}
