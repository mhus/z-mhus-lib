package de.mhus.lib.core.activator;

import java.io.Closeable;
import java.util.HashMap;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.IFlatConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class ActivatorImpl extends MActivator implements MutableActivator {

	protected HashMap<String, Object> mapper = new HashMap<String, Object>();
	protected HashMap<String, Object> instances = new HashMap<String, Object>();

	public ActivatorImpl() {
	}
	
	public ActivatorImpl(ResourceNode cactivator, ClassLoader loader) throws MException {
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

	@Override
	public void addObject(Class<?> ifc, String name, Object obj) {
		if (ifc == null) {
			setInstance(name, obj);
		} else {
			setInstance(ifc.getCanonicalName() + (name != null ? ":" + name : ""), obj);
		}
	}
	
	@Override
	public void setInstance(String name, Object obj) {
		instances.put(name, obj);
	}

	public void addMap(String name, String clazz) {
		if (name == null || clazz == null) return;
		//base.log().t("add map",name,clazz);
		mapper.put(name, clazz);
	}
	
	@Override
	public void addMap(Class<?> ifc,String name, Class<?> clazz) {
		addMap(ifc.getCanonicalName() + ":" + name, clazz);
	}
	
	@Override
	public void addMap(String name, Class<?> clazz) {
		if (name == null || clazz == null) return;
		//base.log().t("add map",name,clazz);
		mapper.put(name, clazz);
	}

	@Override
	public Object mapName(String name) {
		if (name == null) return name;
		if (mapper.containsKey(name))
			return mapper.get(name);
		return name;
	}

	@Override
	public boolean isInstance(String ifc) {
		return instances.containsKey(ifc);
	}

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

	@Override
	protected Object getInstance(String name) {
		return instances.get(name);
	}

	@Override
	public void addMap(Class<?> ifc, Class<?> clazz) {
		addMap(ifc.getCanonicalName(), clazz);
	}

	@Override
	public void removeMap(String name) {
		mapper.remove(name);
	}

	@Override
	public void removeObject(String name) {
		removeObject(null,name);
	}

	@Override
	public void removeObject(Class<?> ifc, String name) {
		if (ifc == null) {
			instances.remove(name);
		} else {
			instances.remove(ifc.getCanonicalName() + (name != null ? ":" + name : ""));
		}
	}
	
	@Override
	public String[] getMapNames() {
		return mapper.keySet().toArray(new String[mapper.size()]);
	}
	
	@Override
	public String[] getObjectNames() {
		return instances.keySet().toArray(new String[mapper.size()]);
	}

}
