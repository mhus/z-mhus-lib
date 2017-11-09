package de.mhus.lib.core.crypt;

import de.mhus.lib.annotations.activator.DefaultImplementation;

@DefaultImplementation(DefaultRandom.class)
public interface MRandom {

	byte getByte();
	int getInt();
	double getDouble();
	
}
