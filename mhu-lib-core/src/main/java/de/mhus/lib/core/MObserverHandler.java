package de.mhus.lib.core;

import java.util.Observer;

public class MObserverHandler extends MEventHandler<Observer> {

	public void fireChanged(Object event) {
		fire(event);
	}

	@Override
	public void fireOn(Observer listener, Object... values) {
		listener.update(null, values[0]);
	}

	
}
