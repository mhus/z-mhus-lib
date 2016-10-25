package de.mhus.lib.cao;

import de.mhus.lib.errors.MException;

public abstract class CaoWritableElement extends CaoNode {

	private static final long serialVersionUID = 1L;
	private CaoNode parent;

	public CaoWritableElement(CaoNode parent) {
		super(parent.getParent());
		this.parent = parent;
	}

	public CaoNode getOriginalElement() {
		return parent;
	}
	
	public abstract CaoOperation getUpdateOperation() throws MException;

}
