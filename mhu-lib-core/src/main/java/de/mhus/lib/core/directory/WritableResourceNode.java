package de.mhus.lib.core.directory;

import de.mhus.lib.errors.MException;

/**
 * <p>Abstract WritableResourceNode class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class WritableResourceNode extends ResourceNode {

	/** Constant <code>MOVE_UP=-100</code> */
	public static final int MOVE_UP = -100;	
	/** Constant <code>MOVE_DOWN=-101</code> */
	public static final int MOVE_DOWN = -101;
	/** Constant <code>MOVE_FIRST=-102</code> */
	public static final int MOVE_FIRST = -102;
	/** Constant <code>MOVE_LAST=-103</code> */
	public static final int MOVE_LAST = -103;

	/**
	 * Create a new config and append it at the end of the list. After creation the element is already at the end of the
	 * list.
	 *
	 * @param key a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if editing is not possible
	 * @return a {@link de.mhus.lib.core.directory.WritableResourceNode} object.
	 */
	public abstract WritableResourceNode createConfig(String key) throws MException;
	
	/**
	 * Move the position of the configuration in the hole set of configurations. If the config
	 * is not part of the set it will throw an exception. The first position is 0.
	 *
	 * @param config The config object which you want to move
	 * @param newPos Absolute new position or one of the move operators MOVE_UP, MOVE_DOWN, MOVE_FIRST, MOVE_LAST
	 * @return The new pos of the config
	 * @throws de.mhus.lib.errors.MException if editing is not possible or the position is out of range
	 */
	public abstract int moveConfig(ResourceNode config, int newPos) throws MException;
	
	/**
	 * Remove a config element from the set. If the config element is not part of the set it will
	 * not throw an error.
	 *
	 * @param config The config object which should be removed.
	 * @throws de.mhus.lib.errors.MException if editing is not possible
	 */
	public abstract void removeConfig(ResourceNode config) throws MException;

	/** {@inheritDoc} */
	@Override
	public void setString(String name, String value) {
		synchronized (this) {
			if (compiledCache!=null)
				compiledCache.remove(name);
		}
		super.setString(name, value);
	}

}
