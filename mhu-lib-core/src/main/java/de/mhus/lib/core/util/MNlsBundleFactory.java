package de.mhus.lib.core.util;

public class MNlsBundleFactory {

	public MNlsBundle create(Object owner) {
		return new MNlsFactory().setOwner(owner);
	}

}
