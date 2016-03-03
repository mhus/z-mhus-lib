package de.mhus.lib.core.util;

import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;

/**
 * <p>WeakObservable class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class WeakObservable extends Observable {
    private boolean changed = false;
    private WeakHashMap<Observer, String> obs;
   
    /**
     * <p>Constructor for WeakObservable.</p>
     */
    public WeakObservable() {
    	obs = new WeakHashMap<Observer, String>();
    }

    /**
     * {@inheritDoc}
     *
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     */
    @Override
	public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
		if (!obs.containsKey(o)) {
		    obs.put(o,"");
		}
    }

    /**
     * {@inheritDoc}
     *
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     */
    @Override
	public synchronized void deleteObserver(Observer o) {
        obs.remove(o);
    }

    /**
     * {@inheritDoc}
     *
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#hasChanged()
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#hasChanged()
     * @see     java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
	public void notifyObservers() {
    	notifyObservers(null);
    }

    /**
     * {@inheritDoc}
     *
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#hasChanged()
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#hasChanged()
     * @see     java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
	public void notifyObservers(Object arg) {
	/*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

	synchronized (this) {
	    /* We don't want the Observer doing callbacks into
	     * arbitrary code while holding its own Monitor.
	     * The code where we extract each Observable from 
	     * the Vector and store the state of the Observer
	     * needs synchronization, but notifying observers
	     * does not (should not).  The worst result of any 
	     * potential race-condition here is that:
	     * 1) a newly-added Observer will miss a
	     *   notification in progress
	     * 2) a recently unregistered Observer will be
	     *   wrongly notified when it doesn't care
	     */
	    if (!changed)
                return;
            arrLocal = obs.keySet().toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((Observer)arrLocal[i]).update(this, arg);
    }

    /**
     * {@inheritDoc}
     *
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
	public synchronized void deleteObservers() {
    	obs.clear();
    }

    /**
     * {@inheritDoc}
     *
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    @Override
	public synchronized void setChanged() {
    	changed = true;
    }

    /**
     * {@inheritDoc}
     *
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     * @see     java.util.Observable#notifyObservers()
     * @see     java.util.Observable#notifyObservers(java.lang.Object)
     * @see     java.util.Observable#notifyObservers()
     * @see     java.util.Observable#notifyObservers(java.lang.Object)
     */
    @Override
	protected synchronized void clearChanged() {
    	changed = false;
    }

    /**
     * {@inheritDoc}
     *
     * Tests if this object has changed.
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#setChanged()
     * @see     java.util.Observable#clearChanged()
     * @see     java.util.Observable#setChanged()
     */
    @Override
	public synchronized boolean hasChanged() {
    	return changed;
    }

    /**
     * {@inheritDoc}
     *
     * Returns the number of observers of this <tt>Observable</tt> object.
     */
    @Override
	public synchronized int countObservers() {
    	return obs.size();
    }
}
