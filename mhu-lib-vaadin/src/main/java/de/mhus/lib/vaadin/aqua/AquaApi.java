package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.IProperties;

public interface AquaApi {
	
	boolean openSpace(String spaceId, String subSpace, String search);
	boolean hasSpace(String spaceId, String subSpace);
	
}
