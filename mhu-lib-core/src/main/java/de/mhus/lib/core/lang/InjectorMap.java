package de.mhus.lib.core.lang;

import java.util.HashMap;
import java.util.Map;

public class InjectorMap implements Injector {

	private HashMap<Class<?>, Injector> injectors = new HashMap<Class<?>, Injector>();
	
	@Override
	public void doInject(Object obj) throws Exception {
		if (obj == null) return;
		for (Map.Entry<Class<?>, Injector> i : injectors.entrySet()) {
			if (i.getKey().isInstance(obj))
				i.getValue().doInject(obj);
		}
	}
	
	public void put(Class<?> clazz, Injector injector) {
		injectors.put(clazz, injector);
	}
	
	public Injector get(Class<?> clazz) {
		return injectors.get(clazz);
	}
	
	public void remove(Class<?> clazz) {
		injectors.remove(clazz);
	}
	
}
