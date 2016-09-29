package de.mhus.lib.core.service;

/**
 * <p>UniqueId class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class UniqueId {

	private int unique = 0;

	/**
	 * <p>nextUniqueId.</p>
	 *
	 * @return a long.
	 */
	public long nextUniqueId() {
		return unique++;
	}

}
