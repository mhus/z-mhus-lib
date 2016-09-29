package de.mhus.lib.core.security;

import java.security.Principal;

/**
 * <p>SimplePrincipal class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class SimplePrincipal implements Principal {

	private String name;
	
	/**
	 * <p>Constructor for SimplePrincipal.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public SimplePrincipal(String name) {
		this.name = name;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return name;
	}
	
	/** {@inheritDoc} */
	public boolean equals(Object in) {
		if (in != null && in instanceof Principal)
			return ((Principal)in).getName().equals(name);
		return super.equals(in);
	}
}
