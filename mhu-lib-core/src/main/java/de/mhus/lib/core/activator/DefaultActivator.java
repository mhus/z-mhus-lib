package de.mhus.lib.core.activator;

import java.io.Closeable;
import java.util.HashMap;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IFlatConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>DefaultActivator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultActivator extends MActivator implements MutableActivator {

	protected HashMap<String, Object> mapper = new HashMap<String, Object>();
	protected HashMap<String, Object> instances = new HashMap<String, Object>();

	/**
	 * <p>Constructor for DefaultActivator.</p>
	 */
	public DefaultActivator() {
	}
	
	/**
	 * <p>Constructor for DefaultActivator.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public DefaultActivator(ClassLoader loader) {
		super(loader);
	}
	
	/**
	 * <p>Constructor for DefaultActivator.</p>
	 *
	 * @param cactivator a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @param loader a {@link java.lang.ClassLoader} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public DefaultActivator(ResourceNode cactivator, ClassLoader loader) throws MException {
		super(loader);
		if (cactivator != null) {
			if (cactivator instanceof IFlatConfig) {
				for (String key : cactivator.getPropertyKeys())
					addMap(key, cactivator.getExtracted(key));
			} else {
				for (ResourceNode entry : cactivator.getNodes("map"))
					addMap(entry.getExtracted("name",""), entry.getExtracted("class",""));
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void addObject(Class<?> ifc, String name, Object obj) {
		if (ifc == null) {
			setInstance(name, obj);
		} else {
			setInstance(ifc.getCanonicalName() + (name != null ? ":" + name : ""), obj);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void setInstance(String name, Object obj) {
		instances.put(name, obj);
	}

	/**
	 * <p>addMap.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param clazz a {@link java.lang.String} object.
	 */
	public void addMap(String name, String clazz) {
		if (name == null || clazz == null) return;
		//base.log().t("add map",name,clazz);
		mapper.put(name, clazz);
	}
	
	/** {@inheritDoc} */
	@Override
	public void addMap(Class<?> ifc,String name, Class<?> clazz) {
		addMap(ifc.getCanonicalName() + ":" + name, clazz);
	}
	
	/** {@inheritDoc} */
	@Override
	public void addMap(String name, Class<?> clazz) {
		if (name == null || clazz == null) return;
		//base.log().t("add map",name,clazz);
		mapper.put(name, clazz);
	}

	/** {@inheritDoc} */
	@Override
	public Object mapName(String name) {
		if (name == null) return name;
		if (mapper.containsKey(name))
			return mapper.get(name);
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isInstance(String ifc) {
		return instances.containsKey(ifc);
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
		super.destroy();
		for (Object obj : instances.values() )
			if (obj instanceof Closeable) {
				try {
					((Closeable)obj).close();
				} catch (Throwable t) {}
			}
		mapper = null;
	}

	/** {@inheritDoc} */
	@Override
	protected Object getInstance(String name) {
		return instances.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public void addMap(Class<?> ifc, Class<?> clazz) {
		addMap(ifc.getCanonicalName(), clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void removeMap(String name) {
		mapper.remove(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeObject(String name) {
		removeObject(null,name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeObject(Class<?> ifc, String name) {
		if (ifc == null) {
			instances.remove(name);
		} else {
			instances.remove(ifc.getCanonicalName() + (name != null ? ":" + name : ""));
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getMapNames() {
		return mapper.keySet().toArray(new String[mapper.size()]);
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getObjectNames() {
		return instances.keySet().toArray(new String[mapper.size()]);
	}

}
