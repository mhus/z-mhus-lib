package de.mhus.lib.cao;

import java.util.LinkedList;

public class CaoList extends LinkedList<CaoNode> {

	private CaoNode parent;

	public CaoList(CaoNode parent) {
		this.parent = parent;
	}
	
	public CaoNode getParent() {
		return parent;
	}

}
