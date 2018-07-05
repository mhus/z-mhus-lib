package de.mhus.lib.core.security;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.util.Address;

public class AccountAttributeMapper extends Address {
	
	private Account acc;

	public AccountAttributeMapper(Account acc) {
		super(MProperties.toIProperties(acc.getAttributes()));
		this.acc = acc;
	}
	
	public String getDisplayName() {
		return acc.getDisplayName();
	}
	
	public String getName() {
		return acc.getName();
	}
	
}
