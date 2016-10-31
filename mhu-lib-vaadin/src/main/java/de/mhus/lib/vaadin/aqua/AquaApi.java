package de.mhus.lib.vaadin.aqua;

public interface AquaApi {
	
	boolean openSpace(String spaceId, String subSpace, String search);
	boolean hasSpace(String spaceId, String subSpace);
	
}
