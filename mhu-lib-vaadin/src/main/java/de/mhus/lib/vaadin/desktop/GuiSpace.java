/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
