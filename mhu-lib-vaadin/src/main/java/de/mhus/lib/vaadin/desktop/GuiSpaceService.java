package de.mhus.lib.vaadin.desktop;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar.MenuItem;

import de.mhus.lib.core.security.AccessControl;

public interface GuiSpaceService {

	String getName();
	String getDisplayName();
	AbstractComponent createSpace();
	boolean hasAccess(AccessControl control);
	void createMenu(AbstractComponent space, MenuItem[] menu);
	boolean isHiddenSpace();
	AbstractComponent createTile();
	int getTileSize();
	boolean isHiddenInMenu();
}
