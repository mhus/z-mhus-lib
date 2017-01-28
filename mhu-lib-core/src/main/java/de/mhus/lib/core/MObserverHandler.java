package de.mhus.lib.core;

import java.util.Observer;

public class MObserverHandler extends MEventHandler<Observer> {

	public void fireChanged(Object event) {
		fire(event);
	}

	@Override
	public void onFire(Observer listener, Object event, Object... values) {
		listener.update(null, event);
	}

	
}
