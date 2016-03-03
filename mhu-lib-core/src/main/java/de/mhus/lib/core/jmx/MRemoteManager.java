package de.mhus.lib.core.jmx;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.errors.MException;

/**
 * <p>MRemoteManager class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MRemoteManager extends MObject implements IBase {
	
	private MBeanServer mbs;
	private HashMap<ObjectName,Object> registry = new HashMap<ObjectName, Object>();

	private Housekeeper housekeeper;

	/**
	 * <p>Constructor for MRemoteManager.</p>
	 *
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public MRemoteManager() throws MException {
		housekeeper = new Housekeeper(this);
		ResourceNode config = base(ConfigProvider.class).getConfig(this,new HashConfig());
		base(MHousekeeper.class).register(housekeeper, config.getLong("housekeeper_sleep",30000), true);
	}
	
	/**
	 * <p>register.</p>
	 *
	 * @param object a {@link de.mhus.lib.core.jmx.JmxObject} object.
	 * @throws java.lang.Exception if any.
	 */
	public void register(JmxObject object) throws Exception {
		register(object,false);
	}
	
	/**
	 * <p>register.</p>
	 *
	 * @param object a {@link de.mhus.lib.core.jmx.JmxObject} object.
	 * @param weak a boolean.
	 * @throws java.lang.Exception if any.
	 */
	public void register(JmxObject object,boolean weak) throws Exception {
		if (object instanceof JmxPackage) {
		  ((JmxPackage)object).open(this);
		} else {
			ObjectName on = object.getJmxObjectName();
//			if (registry.containsKey(on)) {
//				log.w("object already registered, remove first",on);
//			} else {
				register(on,object,true,weak);
				object.setJmxRegistered(true);
//			}
		}
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param object a {@link de.mhus.lib.core.jmx.JmxObject} object.
	 * @throws java.lang.Exception if any.
	 */
	public void unregister(JmxObject object) throws Exception {
		if (object instanceof JmxPackage) {
		  ((JmxPackage)object).close();
		} else {
			ObjectName on = object.getJmxObjectName();
			unregister(on);			
			object.setJmxRegistered(false);
		}
	}
	
	/**
	 * <p>register.</p>
	 *
	 * @param name a {@link javax.management.ObjectName} object.
	 * @param object a {@link java.lang.Object} object.
	 * @param capsulate a boolean.
	 * @param weak a boolean.
	 * @throws java.lang.Exception if any.
	 */
	public void register(ObjectName name, Object object,boolean capsulate, boolean weak) throws Exception {
		
		Object proxy = object;
		if (capsulate)
			proxy = new MBeanProxy(object, null, this, name, weak);
		
		synchronized (this) {
			if (mbs != null)
				try {
					log().t("register",name);
					mbs.registerMBean(proxy, name);
				} catch (InstanceAlreadyExistsException e) {
					if (object instanceof JmxObject) {
						
						while (true) {
							String n = ((JmxObject)object).getJmxName();
							int pos = n.lastIndexOf('-');
							int nr = 1;
							if (pos > 0) {
								nr = MCast.toint(n.substring(pos+1),nr) + 1;
								n = n.substring(0,pos);
							}
							((JmxObject)object).setJmxName(n + "-" + nr);
							name = ((JmxObject)object).getJmxObjectName();
							if (proxy instanceof MBeanProxy) ((MBeanProxy)proxy).setName(name);
							try {
								mbs.registerMBean(proxy, name);
								log().t("renamed",name);
								break;
							} catch (InstanceAlreadyExistsException ex) {}
						}
					} else {
						log().d(name,e);
						throw e;
					}
				} catch (Exception e) {
					log().d(name,e);
					throw e;
				}
				registry.put(name, proxy);
		}
		
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param name a {@link javax.management.ObjectName} object.
	 */
	public void unregister(ObjectName name) {
		synchronized (this) {
			registry.remove(name);
			if (mbs != null)
				try {
					log().t("unregister",name);
					mbs.unregisterMBean(name);
				} catch (Exception e) {
					log().i(name,e);
				}
		}
		
	}
	
	/**
	 * <p>open.</p>
	 */
	public void open() {
		synchronized (this) {
			if (mbs != null) return;
			mbs = ManagementFactory.getPlatformMBeanServer();
			for (Map.Entry<ObjectName, Object> item : registry.entrySet()) {
				try {
					log().t("register",item.getKey());
					mbs.registerMBean(item.getValue(), item.getKey());
				} catch (Exception e) {
					log().i(item.getKey(),e);
				}
			}
		}
	}
	
	/**
	 * <p>close.</p>
	 */
	public void close() {
		synchronized (this) {
			if (mbs == null) return;
			for (Map.Entry<ObjectName, Object> item : registry.entrySet()) {
				try {
					log().t("unregister",item.getKey());
					mbs.unregisterMBean(item.getKey());
				} catch (Exception e) {
					log().i(item.getKey(),e);
				}
			}			
			mbs = null;
		}
	}

	/**
	 * <p>isOpen.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isOpen() {
		return mbs != null;
	}

	/**
	 * <p>check.</p>
	 */
	public void check() {
		synchronized (this) {
			for (Map.Entry<ObjectName, Object> item : new HashMap<ObjectName,Object>(registry).entrySet()) {
				if (item.getValue() instanceof MBeanProxy) {
					((MBeanProxy)item.getValue()).check();
				}
			}
		}
	}
	
	/**
	 * <p>getMBeanServer.</p>
	 *
	 * @return a {@link javax.management.MBeanServer} object.
	 */
	public MBeanServer getMBeanServer() {
		return mbs;
	}
	
	private static class Housekeeper extends MHousekeeperTask {

		private WeakReference<MRemoteManager> manager;
		
		private Housekeeper(MRemoteManager manager) {
			this.manager = new WeakReference<MRemoteManager>(manager);
		}
		
		@Override
		public void doit() throws Exception {
			MRemoteManager obj = manager.get();
			if (obj == null) {
				log().t("close");
				cancel();
				return;
			}
			log().t("Housekeeping");
			try {
				obj.check();
			} catch (Throwable t) {
				log().t(t);
			}
		}
		
	}
}
