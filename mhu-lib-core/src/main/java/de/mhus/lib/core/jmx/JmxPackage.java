package de.mhus.lib.core.jmx;

import java.util.HashSet;

/**
 * <p>JmxPackage class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxPackage extends JmxObject {

	private HashSet<JmxObject> registry = new HashSet<JmxObject>();
	private MRemoteManager remoteManager;
	
	/**
	 * <p>open.</p>
	 *
	 * @param remoteManager a {@link de.mhus.lib.core.jmx.MRemoteManager} object.
	 */
	public void open(MRemoteManager remoteManager) {
		this.remoteManager = remoteManager;
		for (JmxObject bo : registry)
			try {
				remoteManager.register(bo);
			} catch (Exception e) {}
	}

	/**
	 * <p>close.</p>
	 */
	public void close() {
		for (JmxObject bo : registry)
			try {
				remoteManager.unregister(bo);
			} catch (Exception e) {}		
		remoteManager = null;
	}

	/**
	 * <p>register.</p>
	 *
	 * @param object a {@link de.mhus.lib.core.jmx.JmxObject} object.
	 * @throws java.lang.Exception if any.
	 */
	public void register(JmxObject object) throws Exception {
		object.setJmxPackage(getJmxPackage());
		if (remoteManager!= null)
			remoteManager.register(object);
		registry.add(object);
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param object a {@link de.mhus.lib.core.jmx.JmxObject} object.
	 * @throws java.lang.Exception if any.
	 */
	public void unregister(JmxObject object) throws Exception {
		if (remoteManager!= null)
			remoteManager.unregister(object);
		registry.remove(object);
	}
	
}
