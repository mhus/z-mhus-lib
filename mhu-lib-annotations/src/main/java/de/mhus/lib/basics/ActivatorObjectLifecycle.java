package de.mhus.lib.basics;

public interface ActivatorObjectLifecycle {

	void objectActivated(String ifcName, Object currentObject);
	
	void objectDeactivated();
}
