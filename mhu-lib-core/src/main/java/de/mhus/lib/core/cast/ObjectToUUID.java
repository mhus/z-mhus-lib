package de.mhus.lib.core.cast;

import java.util.UUID;

public class ObjectToUUID implements Caster<Object,UUID>{

	@Override
	public Class<? extends UUID> getToClass() {
		return UUID.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public UUID cast(Object in, UUID def) {
		if (in == null) return def;
		try {
			return UUID.fromString(String.valueOf(in));
		} catch (Throwable t) {
			return def;
		}
	}

}
