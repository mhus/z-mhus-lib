package de.mhus.lib.core.cast;

import java.sql.Date;

import de.mhus.lib.core.MCast;

/**
 * <p>ObjectToSqlDate class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectToSqlDate implements Caster<Object,Date>{

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
		if (in == null) return def;
		try {
			String ins = MCast.toString(in);
			return MCast.toSqlDate( MCast.toDate(ins, def) );
		} catch (Throwable t) {
			return def;
		}
	}

}
