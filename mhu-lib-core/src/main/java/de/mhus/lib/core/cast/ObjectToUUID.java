package de.mhus.lib.core.cast;

import java.util.UUID;

/**
 * <p>ObjectToUUID class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectToUUID implements Caster<Object,UUID>{

	/** {@inheritDoc} */
	@Override
	public Class<? extends UUID> getToClass() {
		return UUID.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	/** {@inheritDoc} */
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
