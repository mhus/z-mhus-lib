package de.mhus.lib.cao;

import java.util.LinkedList;

/**
 * <p>CaoList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoList extends LinkedList<CaoNode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CaoNode parent;

	/**
	 * <p>Constructor for CaoList.</p>
	 *
	 * @param parent a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoList(CaoNode parent) {
		this.parent = parent;
	}

	/**
	 * <p>Getter for the field <code>parent</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoNode getParent() {
		return parent;
	}

	public CaoList append(CaoNode ... nodes) {
		for (CaoNode node : nodes)
			add(node);
		return this;
	}
	
}
