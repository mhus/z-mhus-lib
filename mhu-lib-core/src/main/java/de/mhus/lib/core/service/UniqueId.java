package de.mhus.lib.core.service;

public class UniqueId {

	private int unique = 0;

	public long nextUniqueId() {
		return unique++;
	}

}
