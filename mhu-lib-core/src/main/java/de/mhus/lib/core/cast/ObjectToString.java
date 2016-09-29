package de.mhus.lib.core.cast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

public class ObjectToString implements Caster<Object,String>{

	@Override
	public Class<? extends String> getToClass() {
		return String.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public String cast(Object in, String def) {
		if (in == null) return def;
		if (in instanceof String) return (String)in;
		if (in instanceof Date) return MDate.toIso8601((Date)in);
		if (in instanceof Calendar) return MDate.toIso8601((Calendar)in);
		if (in instanceof Throwable) return MCast.toString((Throwable)in);
		if (in instanceof byte[]) return MCast.toString((byte[])in);
		if (in.getClass().isArray()) Arrays.asList(in).toString();
		return String.valueOf(in);
	}

}
