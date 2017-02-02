package de.mhus.lib.vaadin.desktop;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar.MenuItem;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.security.AccessControl;

public abstract class GuiSpace extends MLog implements GuiSpaceService {

	@Override
	public boolean hasAccess(AccessControl control) {
		return true;
	}

	@Override
	public void createMenu(AbstractComponent space, MenuItem[] menu) {
	}

	@Override
	public boolean isHiddenSpace() {
		return false;
	}

	@Override
	public AbstractComponent createTile() {
		return null;
	}

	@Override
	public int getTileSize() {
		return 0;
	}

	@Override
	public boolean isHiddenInMenu() {
		return false;
	}

}
