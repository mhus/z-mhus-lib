package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.Component;

public interface WorkingArea {

	void setComponent(Object owner, Component container);
	
	boolean isOwner(Object owner);

}
