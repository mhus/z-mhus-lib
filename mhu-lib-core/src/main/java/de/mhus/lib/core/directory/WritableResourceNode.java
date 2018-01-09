package de.mhus.lib.core.directory;

import de.mhus.lib.errors.MException;

public abstract class WritableResourceNode<W extends WritableResourceNode<W>> extends ResourceNode<W> {

	private static final long serialVersionUID = 1L;
	public static final int MOVE_UP = -100;	
	public static final int MOVE_DOWN = -101;
	public static final int MOVE_FIRST = -102;
	public static final int MOVE_LAST = -103;

	/**
	 * Create a new config and append it at the end of the list. After creation the element is already at the end of the
	 * list.
	 * 
	 * @param key
	 * @return new config node
	 * @throws MException if editing is not possible
	 */
	public abstract WritableResourceNode<W> createConfig(String key) throws MException;
	
	/**
	 * Move the position of the configuration in the hole set of configurations. If the config
	 * is not part of the set it will throw an exception. The first position is 0.
	 * 
	 * @param config The config object which you want to move
	 * @param newPos Absolute new position or one of the move operators MOVE_UP, MOVE_DOWN, MOVE_FIRST, MOVE_LAST
	 * @return The new pos of the config
	 * @throws MException if editing is not possible or the position is out of range
	 */
	public abstract int moveConfig(W config, int newPos) throws MException;
	
	/**
	 * Remove a config element from the set. If the config element is not part of the set it will
	 * not throw an error.
	 * 
	 * @param config The config object which should be removed.
	 * @throws MException if editing is not possible
	 */
	public abstract void removeConfig(W config) throws MException;

	@Override
	public void setString(String name, String value) {
		synchronized (this) {
			if (compiledCache!=null)
				compiledCache.remove(name);
		}
		super.setString(name, value);
	}

}
