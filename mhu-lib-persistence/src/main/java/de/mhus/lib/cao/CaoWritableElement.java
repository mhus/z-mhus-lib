package de.mhus.lib.cao;

import de.mhus.lib.core.directory.WritableResourceNode;

/**
 * <p>Abstract CaoWritableElement class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoWritableElement extends WritableResourceNode {

	private CaoNode parent;

	/**
	 * <p>Constructor for CaoWritableElement.</p>
	 *
	 * @param parent a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoWritableElement(CaoNode parent) {
		this.parent = parent;
	}

	/**
	 * <p>getElement.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoNode getElement() {
		return parent;
	}

}
