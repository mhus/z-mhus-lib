package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoActionList;

public class MutableActionList extends CaoActionList {

	public MutableActionList() {
		
	}
	
	public MutableActionList(CaoActionList copyFrom) {
		for (CaoAction action : copyFrom) {
			add(action);
		}
	}
	
	/**
	 * <p>add.</p>
	 *
	 * @param action a {@link de.mhus.lib.cao.CaoAction} object.
	 */
	public void add(CaoAction action) {
		String name = action.getName();

		if (index.containsKey(name)) {
			//TODO find the best one, maybe replace
			return;
		}

		actions.add(action);
		index.put(name, action);

	}

}
