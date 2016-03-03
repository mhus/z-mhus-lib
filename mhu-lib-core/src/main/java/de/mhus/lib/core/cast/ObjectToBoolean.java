package de.mhus.lib.core.cast;

import de.mhus.lib.core.util.ObjectContainer;

/**
 * <p>ObjectToBoolean class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectToBoolean implements Caster<Object,Boolean>{

	/** {@inheritDoc} */
	@Override
	public Class<? extends Boolean> getToClass() {
		return Boolean.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean cast(Object in, Boolean def) {
		ObjectContainer<Boolean> ret = new ObjectContainer<>(def);
		toBoolean(in, false, ret);
		return ret.getObject();
	}

	/**
	 * <p>toBoolean.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @param def a boolean.
	 * @param ret a {@link de.mhus.lib.core.util.ObjectContainer} object.
	 * @return a boolean.
	 */
	public boolean toBoolean(Object in, boolean def, ObjectContainer<Boolean> ret) {
		if (in == null)
			return def;

		if (in instanceof Boolean)
			return (Boolean)in;
		
		if (in instanceof Number)
			return !(((Number)in).intValue() == 0);
		
		String ins = in.toString().toLowerCase().trim();

		if (
		ins.equals("yes")
				||
		ins.equals("on")
				||
		ins.equals("true")
				||
		ins.equals("ja")  // :-)
				||
		ins.equals("tak") // :-)
				||
		ins.equals("oui") // :-)
				||
		ins.equals("si") // :-)
				||
		ins.equals("\u4fc2") // :-) chinese
				||
		ins.equals("HIja'") // :-) // klingon
				||
		ins.equals("1")
				||
		ins.equals("t")
				||
		ins.equals("y")
				||
		ins.equals("\u2612")
		) {
			if (ret != null) ret.setObject(true);
			return true;
		}

		if (
		ins.equals("no")
				||
		ins.equals("off")
				||
		ins.equals("false")
				||
		ins.equals("nein") // :-)
				||
		ins.equals("nie")  // :-)
				||
		ins.equals("non")  // :-)
				||
		ins.equals("\u5514\u4fc2")  // :-) chinese
				||
		ins.equals("Qo'")  // :-) klingon
				||
		ins.equals("0")
				||
		ins.equals("-1")
				||
		ins.equals("f")
				||
		ins.equals("n")
				||
		ins.equals("\u2610")
				) {
			if (ret != null) ret.setObject(false);
			return false;
		}
		return def;
		
	}
}
