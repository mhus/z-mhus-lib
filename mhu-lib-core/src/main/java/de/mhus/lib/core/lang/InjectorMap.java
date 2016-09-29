package de.mhus.lib.core.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>InjectorMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class InjectorMap implements Injector {

	private HashMap<Class<?>, Injector> injectors = new HashMap<Class<?>, Injector>();
	
	/** {@inheritDoc} */
	@Override
	public void doInject(Object obj) throws Exception {
		if (obj == null) return;
		for (Map.Entry<Class<?>, Injector> i : injectors.entrySet()) {
			if (i.getKey().isInstance(obj))
				i.getValue().doInject(obj);
		}
	}
	
	/**
	 * <p>put.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @param injector a {@link de.mhus.lib.core.lang.Injector} object.
	 */
	public void put(Class<?> clazz, Injector injector) {
		injectors.put(clazz, injector);
	}
	
	/**
	 * <p>get.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a {@link de.mhus.lib.core.lang.Injector} object.
	 */
	public Injector get(Class<?> clazz) {
		return injectors.get(clazz);
	}
	
	/**
	 * <p>remove.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 */
	public void remove(Class<?> clazz) {
		injectors.remove(clazz);
	}
	
}
