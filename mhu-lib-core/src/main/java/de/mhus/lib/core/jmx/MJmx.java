package de.mhus.lib.core.jmx;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

/**
 * <p>MJmx class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MJmx extends JmxObject {

	/**
	 * <p>Constructor for MJmx.</p>
	 */
	public MJmx() {
		this(true,MSystem.findSource(4));
	}
	
	/**
	 * <p>Constructor for MJmx.</p>
	 *
	 * @param weak a boolean.
	 * @param name a {@link java.lang.String} object.
	 */
	public MJmx(boolean weak,String name) {
		jmxRegister(weak, name);
	}
	
	/**
	 * <p>jmxRegister.</p>
	 *
	 * @param weak a boolean.
	 * @param name a {@link java.lang.String} object.
	 */
	protected void jmxRegister(boolean weak,String name) {
		if (!isJmxRegistered()) {
			try {
				setJmxName(name);
				MSingleton.baseLookup(this,MRemoteManager.class).register(this,weak);
			} catch (Exception e) {
				log().t(e);
			}
		}
	}
}
