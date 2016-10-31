/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.WeakHashMap;

public abstract class MEventHandler<L> extends MLog {

	private HashMap<L,String> listeners = new HashMap<L,String>();
	private WeakHashMap<L, String> weak = new WeakHashMap<L, String>();
	private boolean weakHandler = false;
	
	/**
	 * Create the handler in normal mode.
	 */
	public MEventHandler() {
		this(false);
	}
	
	/**
	 * Create the handler and specify if all listeners have to be weak.
	 * 
	 * @param weakHandler If true handler is in weak mode. All registered handlers are weak.
	 */
	public MEventHandler(boolean weakHandler) {
		this.weakHandler = weakHandler;
	}
	/**
	 * Register a listener for this kind of events. If the handler is weak the
	 * listener will registered weak. A listener can only register one time. A second
	 * registration will be ignored.
	 * 
	 * @param listener Listener for the events.
	 */
	public void register(L listener) {
		if (weakHandler) {
			registerWeak(listener);
			return;
		}
		synchronized (listeners) {
			if (!contains(listener))
				listeners.put(listener,"");
		}
	}

	/**
	 * Unregister normal or weak listener from the handler.
	 * 
	 * @param listener Listener for the events.
	 */
	public void unregister(L listener) {
		synchronized (listeners) {
			listeners.remove(listener);
			weak.remove(listener);
		}
	}

	/**
	 * Register the listener as a weak reference. 
	 * 
	 * @param listener Listener for the events.
	 */
	public void registerWeak(L listener) {
		synchronized (listeners) {
			weak.put(listener, "");
		}
	}

	/**
	 * Returns true if the listener instance is already registered for this
	 * event handler.
	 * 
	 * @param listener Listener for the events.
	 * @return true if the listener is registered as normal or weak.
	 */
	public boolean contains(L listener) {
		synchronized (this) {
			return listeners.containsKey(listener) || weak.containsKey(listener);
		}
	}

	/**
	 * Returns a array of all registered (normal or weak) listeners. The list should not be
	 * cashed - this will prevent weak listeners to be removed from the memory. The list
	 * should be used to fire events to the listeners if the listeners are able to change the
	 * event handler (register / unregister).
	 * 
	 * @return All registered listeners
	 */
	public Object[] getListenersArray() {
		Object[] list = null;
		synchronized (listeners) {			
			Object[] list1 = weakHandler ? null : listeners.keySet().toArray();
			Object[] list2 = weak.keySet().toArray();
			if ( list1 == null )
				list = list2;
			else if (list2.length == 0)
				list = list1;
			else if (list1.length == 0)
				list = list2;
			else {
				list = new Object[list1.length + list2.length];
				System.arraycopy(list1, 0, list, 0, list1.length);
				System.arraycopy(list2, 0, list, list1.length, list2.length);
			}
		}
		return list;
	}

	/**
	 * Use this to iterate into the listeners and if you be sure the listeners
	 * do not try to change the event handler (register/unregister). This
	 * method will save resources if only normal or only weak listeners are 
	 * registered.
	 * 
	 * @return Iterable object for all listeners.
	 */
	public Iterable<L> getListeners() {
		if (weakHandler || listeners.size() == 0)
			return weak.keySet();
		if (weak.size() == 0)
			return listeners.keySet();
		LinkedList<L> ll = new LinkedList<L>(listeners.keySet());
		ll.addAll(weak.keySet());
		return ll;
	}
	
	/**
	 * Amount of registered listeners.
	 * 
	 * @return
	 */
	public int size() {
		return listeners.size() + weak.size();
	}
	
	/**
	 * Return true if the handler only store weak references (also by register()).
	 * 
	 * @return flag
	 */
	public boolean isWeakHandler() {
		return weakHandler;
	}

	public void fire(Object ... values) {
		for (Object obj : getListenersArray()) {
			try {
				onFire((L)obj, values);
//				method.invoke(obj, values);
			} catch (Throwable t) {
				log().d(obj,values);
			}
		}
	}
	
	public abstract void onFire(L listener, Object ... values);
	
}
