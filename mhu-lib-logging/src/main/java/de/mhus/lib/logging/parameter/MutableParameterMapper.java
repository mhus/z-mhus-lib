package de.mhus.lib.logging.parameter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.util.SoftHashMap;

/**
 * <p>MutableParameterMapper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class MutableParameterMapper extends AbstractParameterMapper implements de.mhus.lib.core.logging.MutableParameterMapper {

	private HashMap<String, ParameterEntryMapper> mapping = new HashMap<>();
	private SoftHashMap<String, ParameterEntryMapper> cache = new SoftHashMap<>();
	private ParameterEntryMapper noMapper = new ParameterEntryMapper() {
		
		@Override
		public Object map(Object in) {
			return null;
		}
	};
	
	/** {@inheritDoc} */
	@Override
	protected Object map(Object o) {
		if (o == null || mapping.size() == 0) return null;
		Class<?> c = o.getClass();
		if (c.isPrimitive()) return null;
		String name = o.getClass().getCanonicalName();
		if (name.startsWith("java.lang")) return null;
		
		synchronized (this) {
			ParameterEntryMapper mapper = cache.get(name);
			if (mapper != null) return mapper.map(o);
		}
		
		while (c != null) {

			String n = c.getCanonicalName();
			ParameterEntryMapper mapper = mapping.get(n);
			if (mapper != null) {
				synchronized (this) {
					cache.put(name, mapper);
					return mapper.map(o);
				}
			}
			
			List<Class<?>> l = new LinkedList<>();
			findInterfaces(c, l);
			for (Class<?> i : l) {
				
				n = i.getCanonicalName();
				mapper = mapping.get(n);
				
				if (mapper != null) {
					synchronized (this) {
						cache.put(name, mapper);
						return mapper.map(o);
					}
				}
				
								
			}
			
			c = c.getSuperclass();
		}

		synchronized (this) {
			cache.put(name, noMapper);
		}
		return null;
	}

	private void findInterfaces(Class<?> c, List<Class<?>> list) {
		for (Class<?> i : c.getInterfaces()) {
			list.add(i);
			findInterfaces(i, list);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		synchronized (this) {
			mapping.clear();
			cache.clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void put(String clazz, ParameterEntryMapper mapper) {
		synchronized (this) {
			mapping.put(clazz, mapper);
			cache.remove(clazz);
		}
	}

}
