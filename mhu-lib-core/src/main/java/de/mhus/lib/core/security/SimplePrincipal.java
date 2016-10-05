package de.mhus.lib.core.security;

import java.security.Principal;

public class SimplePrincipal implements Principal {

	private String name;
	
	public SimplePrincipal(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
	
	public boolean equals(Object in) {
		if (in != null && in instanceof Principal)
			return ((Principal)in).getName().equals(name);
		return super.equals(in);
	}
}
