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
package de.mhus.lib.vaadin;

import com.vaadin.ui.UI;

import de.mhus.lib.core.security.JaasAccessControl;
import de.mhus.lib.vaadin.servlet.VaadinSessionWrapper;

public class VaadinAccessControl extends JaasAccessControl {

	public VaadinAccessControl(String realm) {
		super(realm, new VaadinSessionWrapper(UI.getCurrent().getSession() ) );
	}


}
