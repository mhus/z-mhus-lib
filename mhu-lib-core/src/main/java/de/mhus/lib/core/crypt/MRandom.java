package de.mhus.lib.core.crypt;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.errors.NotSupportedException;

@DefaultImplementation(DefaultRandom.class)
public interface MRandom {

	/**
	 * Return a random byte from -127 to 128
	 * @return
	 */
	byte getByte();
	
	/**
	 * Return a random integer from 0 to INTEGER MAX.
	 * @return
	 */
	int getInt();
	
	/**
	 * Return a random double from 0 to 1
	 * @return
	 */
	double getDouble();
	
	/**
	 * Return a random long from 0 to LONG MAX.
	 * @return
	 */
	long getLong();

	/**
	 * Return an adaption of an random if available.
	 * e.g. java.util.Random and java.security.Random should be supported.
	 * 
	 * @param ifc Requested Interface or Class
	 * @return The instance of Ifc
	 * @throws NotSupportedException If adaption was not possible.
	 */
	<T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException; // adaptTo java.util.Random or java.secure.Random if available
	
}
