package de.mhus.lib.core.cast;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.ObjectContainer;

/**
 * <p>ObjectToDouble class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectToDouble implements Caster<Object,Double> {

	private final static Log log = Log.getLog(ObjectToDouble.class);
	
	/** {@inheritDoc} */
	@Override
	public Class<? extends Double> getToClass() {
		return Double.class;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	/** {@inheritDoc} */
	@Override
	public Double cast(Object in, Double def) {
		ObjectContainer<Double> ret = new ObjectContainer<>(def);
		toDouble(in, 0, ret);
		return ret.getObject();
	}

	/**
	 * <p>toDouble.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @param def a double.
	 * @param ret a {@link de.mhus.lib.core.util.ObjectContainer} object.
	 * @return a double.
	 */
	public double toDouble(Object in, double def, ObjectContainer<Double> ret) {
		if (in == null) return def;
		if (in instanceof Number) {
			double r = ((Number)in).doubleValue();
			if (ret != null) ret.setObject(r);
			return r;
		}
		try {
			double r = Double.parseDouble(String.valueOf(in));
			if (ret != null) ret.setObject(r);
			return r;
		} catch (Throwable e) {
			log.t(in,e.toString());
		}
		return def;
	}
	
}
