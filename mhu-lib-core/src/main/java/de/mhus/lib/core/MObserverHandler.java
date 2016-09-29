package de.mhus.lib.core;

import java.util.Observer;

/**
 * <p>MObserverHandler class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MObserverHandler extends MEventHandler<Observer> {

	/**
	 * <p>fireChanged.</p>
	 *
	 * @param event a {@link java.lang.Object} object.
	 */
	public void fireChanged(Object event) {
		fire(event);
	}

	/** {@inheritDoc} */
	@Override
	public void onFire(Observer listener, Object... values) {
		listener.update(null, values[0]);
	}

	
}
