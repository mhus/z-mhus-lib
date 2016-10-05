package de.mhus.lib.core.jmx;

import java.util.HashSet;

public class JmxPackage extends JmxObject {

	private HashSet<JmxObject> registry = new HashSet<JmxObject>();
	private MRemoteManager remoteManager;
	
	public void open(MRemoteManager remoteManager) {
		this.remoteManager = remoteManager;
		for (JmxObject bo : registry)
			try {
				remoteManager.register(bo);
			} catch (Exception e) {}
	}

	public void close() {
		for (JmxObject bo : registry)
			try {
				remoteManager.unregister(bo);
			} catch (Exception e) {}		
		remoteManager = null;
	}

	public void register(JmxObject object) throws Exception {
		object.setJmxPackage(getJmxPackage());
		if (remoteManager!= null)
			remoteManager.register(object);
		registry.add(object);
	}

	public void unregister(JmxObject object) throws Exception {
		if (remoteManager!= null)
			remoteManager.unregister(object);
		registry.remove(object);
	}
	
}
