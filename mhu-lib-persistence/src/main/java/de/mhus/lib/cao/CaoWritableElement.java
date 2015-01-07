package de.mhus.lib.cao;

import de.mhus.lib.core.directory.WritableResourceNode;

public abstract class CaoWritableElement extends WritableResourceNode {

	private CaoNode parent;

	public CaoWritableElement(CaoNode parent) {
		this.parent = parent;
	}
	
	public CaoNode getElement() {
		return parent;
	}
	
}
