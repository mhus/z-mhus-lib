package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar.MenuItem;

import de.mhus.lib.core.security.AccessControl;

public interface AquaSpace {

	String getName();
	String getDisplayName();
	AbstractComponent createSpace();
	boolean hasAccess(AccessControl control);
	void createMenu(MenuItem menu);
	
}
