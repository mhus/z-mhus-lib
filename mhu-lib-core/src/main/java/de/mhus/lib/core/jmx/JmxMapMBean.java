package de.mhus.lib.core.jmx;

/**
 * <p>JmxMapMBean interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface JmxMapMBean extends JmxObjectMBean {

	/**
	 * <p>getSize.</p>
	 *
	 * @return a int.
	 */
	public int getSize();
	
	/**
	 * <p>getEntries.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getEntries();
	
	/**
	 * <p>clear.</p>
	 */
	public void clear();
	
	/**
	 * <p>remove.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	public void remove(String key);
	
}
