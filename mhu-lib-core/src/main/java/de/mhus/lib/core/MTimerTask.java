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

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;

/**
 * <p>Abstract MTimerTask class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class MTimerTask extends TimerTask implements Observer, Named {

	private boolean canceled = false;
	private String name;
	
	/**
	 * <p>Constructor for MTimerTask.</p>
	 */
	public MTimerTask() {
		setName(MSystem.getClassName(this));
	}
		
	/** {@inheritDoc} */
	@Override
	final public void run() {
		boolean error = false;
		try {
			doit();
		} catch (Throwable t) {
			try {
				onError(t);
			} catch (Throwable t1) {
			}
			error = true;
		}
		try {
			onFinal(error);
		} catch (Throwable t) {
		}
	}

	/**
	 * <p>onError.</p>
	 *
	 * @param t a {@link java.lang.Throwable} object.
	 */
	protected void onError(Throwable t) {
		t.printStackTrace();
	}

	/**
	 * <p>onFinal.</p>
	 *
	 * @param isError a boolean.
	 */
	protected void onFinal(boolean isError) {
	}

	/**
	 * <p>doit.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	public abstract void doit() throws Exception;
	
    /** {@inheritDoc} */
    @Override
	public void update(Observable o, Object arg) {
    	run();
    }

    /** {@inheritDoc} */
    @Override
    public boolean cancel() {
    	setCanceled(true);
    	return super.cancel();
    }
    
	/**
	 * <p>isCanceled.</p>
	 *
	 * @return a boolean.
	 * @since 3.2.9
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * <p>Setter for the field <code>canceled</code>.</p>
	 *
	 * @param canceled a boolean.
	 * @since 3.2.9
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public void setName(String name) {
		this.name = name;
	}

}
