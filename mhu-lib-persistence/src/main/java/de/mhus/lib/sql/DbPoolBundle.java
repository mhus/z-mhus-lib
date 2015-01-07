package de.mhus.lib.sql;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * The class holds a bundle of different database pools.
 * 
 * @author mikehummel
 *
 */
public class DbPoolBundle extends MObject {

	
	private ResourceNode config;
	private MActivator activator;
	private Map<String, DbPool> bundle = new HashMap<String, DbPool>();
	
	/**
	 * Create a new bundle from default configuration. Load it from MSingleton with the
	 * key of this class.
	 * 
	 */
	public DbPoolBundle() {
		this(null,null);
	}
	
	/**
	 * Create a new Bundle from configuration.
	 * 
	 * @param config Config element or null. null will use the central MSingleton configuration.
	 * @param activator Activator or null. null will use the central MSingleton activator.
	 */
	public DbPoolBundle(ResourceNode config, MActivator activator) {

		if (config == null) config = base(ConfigProvider.class).getConfig(DbPoolBundle.class, null);
		if (activator == null) activator = base(MActivator.class);

		this.config = config;
		this.activator = activator;
	}
	
	public DbPool getPool(String name) throws Exception {
		
		if (bundle == null) throw new MException("Bundle already closed");

		synchronized (bundle) {
			DbPool pool = bundle.get(name);
			if (pool == null) {
				ResourceNode poolCon = config.getNode(name);
				if (poolCon != null) {
					pool = new DefaultDbPool(poolCon, activator);
					bundle.put(name, pool);
				} else {
					throw new MException("pool config not found",name);
				}
			}
			return pool;
		}
	}
	
	public String[] getNames() {
		LinkedList<String> out = new LinkedList<String>();
		for (ResourceNode c : config.getNodes()) {
			try {
				out.add(c.getName());
			} catch (MException e) {
				throw new MRuntimeException(e);
			}
		}
		return out.toArray(new String[out.size()]);
	}
	
	public ResourceNode getConfig(String name) {
		return config.getNode(name);
	}
	
	public ResourceNode getConfig() {
		return config;
	}
	
	public void reset() {
		bundle = new HashMap<String, DbPool>();
	}
	
	public void close() {
		
		if (bundle == null) return;
		
		synchronized (bundle) {
			for (DbPool pool : bundle.values())
				pool.close();
			bundle = null;
		}
	}
	
}
