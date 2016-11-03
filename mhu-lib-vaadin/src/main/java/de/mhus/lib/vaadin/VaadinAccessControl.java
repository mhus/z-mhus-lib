package de.mhus.lib.vaadin;

import com.vaadin.ui.UI;

import de.mhus.lib.core.security.JaasAccessControl;
import de.mhus.lib.vaadin.servlet.VaadinSessionWrapper;

public class VaadinAccessControl extends JaasAccessControl {

	public VaadinAccessControl(String realm) {
		super(realm, new VaadinSessionWrapper(UI.getCurrent().getSession() ) );
	}


}
