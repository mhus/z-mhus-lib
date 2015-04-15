package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.ComponentContainer;

public interface NavigationNode {

	NavigationNode getParent();
	
	NavigationNode[] getChildren();
	
	String getTitle();
	
	boolean isLeaf();
	
	boolean equals(Object other);

	void doShowPreview(Desktop desktop, ComponentContainer previewContentPane);

	void doOpenNode(Desktop desktop);

}
