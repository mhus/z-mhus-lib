package de.mhus.lib.core.jmx;

/**
 * <p>JmxListMBean interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface JmxListMBean extends JmxObjectMBean {

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
	 * @param nr a int.
	 */
	public void remove(int nr);
	
}
