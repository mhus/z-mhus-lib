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
		for (Object ob : getListenersArray()) {
			try {
				((Observer)ob).update(null, event);
			} catch (Throwable t) {
				log().w("fireChanged",event,t);
			}
		}
	}

	
}
