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

import java.util.Locale;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar.MenuItem;

import de.mhus.lib.core.security.AccessControl;

public interface GuiSpaceService {

	/**
	 * Return the technical unique name of the space.
	 * @return the name
	 */
	String getName();
	/**
	 * Return the display name for the country.
	 * @param locale 
	 * @return display name
	 */
	String getDisplayName(Locale locale);
	/**
	 * Create a space UI component.
	 * @return the space
	 */
	AbstractComponent createSpace();
	/**
	 * Return true if the current user have access. Return true if you are unsure.
	 * The desktop will have a separate access control.
	 * @param control
	 * @return true if has access
	 */
	boolean hasAccess(AccessControl control);
	/**
	 * This method is called EVERY time the space is shown.
	 * It allows the space to create a custom menu. Best practice
	 * is to delegate the call into the space object.
	 * 
	 * @param space
	 * @param menu
	 */
	void createMenu(AbstractComponent space, MenuItem[] menu);
	/**
	 * If the space should not have a preview return true.
	 * @return true if spase shoild not be shown
	 */
	boolean isHiddenSpace();
	/**
	 * If the service needs to create a custom tile preview return
	 * a preview component. If not return null.
	 * @return the title
	 */
	AbstractComponent createTile();
	/**
	 * Return the width of the preview tile. The with could be between
	 * 1 and 3. If you are not sure, return 0.
	 * @return size in blocks
	 */
	int getTileSize();
	/**
	 * Return true if this space should not be shown in the spaces menu.
	 * @return true if not shown in menu
	 */
	boolean isHiddenInMenu();
	/**
	 * Creates a new Help Context instance for the given locale or a fallback instance.
	 * If the service do not support help. The function should return null.
	 * 
	 * @param locale
	 * 
	 * @return help context or null
	 */
	HelpContext createHelpContext(Locale locale);
}
