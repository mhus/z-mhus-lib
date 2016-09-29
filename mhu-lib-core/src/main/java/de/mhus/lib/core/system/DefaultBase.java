package de.mhus.lib.core.system;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.lang.Base;

/**
 * <p>DefaultBase class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultBase extends Base {
	
	MActivator activator;

	/**
	 * <p>Constructor for DefaultBase.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public DefaultBase(Base parent) {
		super(parent);
		this.activator = MSingleton.get().createActivator();
	}
	
	/** {@inheritDoc} */
	@Override
	public void destroy() {
		if (activator != null) activator.destroy();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isDestroyed() {
		return activator == null || activator.isDestroyed();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBase(Class<?> ifc) {
		return activator != null && activator.isInstance(ifc) || parent != null && parent.isBase(ifc);
	}

	/**
	 * <p>Getter for the field <code>activator</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MActivator} object.
	 */
	public MActivator getActivator() {
		return activator;
	}

	/** {@inheritDoc} */
	@Override
	public <T> T lookup(Class<T> ifc) {
		try {
			if (activator == null) {
				if (parent != null)
					return parent.lookup(ifc);
				return null;
			}
			
			if (parent != null && !activator.isInstance(ifc) && ( local == null || !local.contains(ifc.getCanonicalName()) ) )
				return parent.lookup(ifc);
			
			return activator.getObject(ifc);
		} catch (Exception e) {
			e.printStackTrace(); // TODO use logger
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void addObject(Class<?> ifc, Object obj) {
		MActivator act = getActivator();
		if (act instanceof MutableActivator)
			((MutableActivator)act).addObject(ifc, null, obj);
	}

	/** {@inheritDoc} */
	@Override
	public void removeObject(Class<?> ifc) {
		MActivator act = getActivator();
		if (act instanceof MutableActivator)
			((MutableActivator)act).removeObject(ifc, null);
		
	}

}
