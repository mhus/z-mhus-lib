package de.mhus.lib.vaadin.aqua;

import java.util.LinkedList;

public class NavigationSelection {

	protected NavigationNode[] path;
	protected NavigationNode node;
	private NavigationNode parent;

	public NavigationSelection(NavigationNode node) {
		
		this.node = node;

		this.parent = node;
		if (node.isLeaf()) 
			parent = node.getParent();
		
		LinkedList<NavigationNode> cache = new LinkedList<NavigationNode>();
		NavigationNode cur = parent;
		while (cur != null) {
			cache.addFirst(cur);
			cur = cur.getParent();
		}
		path = cache.toArray(new NavigationNode[cache.size()]);

	}
	
	public NavigationNode[] getPath() {
		return path;
	}
	
	public NavigationNode getSelectedNode() {
		return node;
	}
	
	public NavigationNode[] getChildren() {
		return parent.getChildren();
	}
	
	public boolean isSelectedNode(NavigationNode test) {
		return node.equals(test);
	}
	
}
