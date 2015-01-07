package de.mhus.lib.core.system;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.lang.Base;

public class DefaultBase extends Base {
	
	MActivator activator;

	public DefaultBase(Base parent) {
		super(parent);
		this.activator = MSingleton.get().createActivator();
	}
	
	@Override
	public void destroy() {
		if (activator != null) activator.destroy();
	}

	@Override
	public boolean isDestroyed() {
		return activator == null || activator.isDestroyed();
	}

	@Override
	public boolean isBase(Class<?> ifc) {
		return activator != null && activator.isInstance(ifc) || parent != null && parent.isBase(ifc);
	}

	public MActivator getActivator() {
		return activator;
	}

	@Override
	public <T> T base(Class<T> ifc) {
		try {
			if (activator == null) {
				if (parent != null)
					return parent.base(ifc);
				return null;
			}
			
			if (parent != null && !activator.isInstance(ifc) && ( local == null || !local.contains(ifc.getCanonicalName()) ) )
				return parent.base(ifc);
			
			return activator.getObject(ifc);
		} catch (Exception e) {
			e.printStackTrace(); // TODO use logger
		}
		return null;
	}

	@Override
	public void addObject(Class<?> ifc, Object obj) {
		MActivator act = getActivator();
		if (act instanceof MutableActivator)
			((MutableActivator)act).addObject(ifc, null, obj);
	}

	@Override
	public void removeObject(Class<?> ifc) {
		MActivator act = getActivator();
		if (act instanceof MutableActivator)
			((MutableActivator)act).removeObject(ifc, null);
		
	}

}
