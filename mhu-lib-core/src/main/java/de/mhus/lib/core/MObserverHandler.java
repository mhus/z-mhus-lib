package de.mhus.lib.core;

import java.util.Observer;

public class MObserverHandler extends MEventHandler<Observer> {

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
