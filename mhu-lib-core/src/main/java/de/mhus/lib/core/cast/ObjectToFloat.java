package de.mhus.lib.core.cast;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.ObjectContainer;

public class ObjectToFloat implements Caster<Object,Float> {

	private final static Log log = Log.getLog(ObjectToFloat.class);
	
	@Override
	public Class<? extends Float> getToClass() {
		return Float.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public Float cast(Object in, Float def) {
		ObjectContainer<Float> ret = new ObjectContainer<>(def);
		toFloat(in, 0, ret);
		return ret.getObject();
	}

	public float toFloat(Object in, float def, ObjectContainer<Float> ret) {
		if (in == null) return def;
		if (in instanceof Number) {
			float r = ((Number)in).floatValue();
			if (ret != null) ret.setObject(r);
			return r;
		}
		try {
			float r = Float.parseFloat(String.valueOf(in));
			if (ret != null) ret.setObject(r);
			return r;
		} catch (Throwable e) {
			log.t(in,e.toString());
		}
		return def;
	}
	
}
