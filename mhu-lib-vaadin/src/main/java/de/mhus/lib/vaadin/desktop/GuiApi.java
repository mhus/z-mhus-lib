package de.mhus.lib.vaadin.desktop;

import javax.security.auth.Subject;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.security.AccessControl;

public interface GuiApi {

	boolean hasAccess(String role);
		
	Subject getCurrentUser();
	
	String getHost();

	AccessControl getAccessControl();
	
}
