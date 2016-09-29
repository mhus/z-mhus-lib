package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

public class DefRoot extends DefComponent {

	private static final long serialVersionUID = 1L;
	public static final String ROOT = "root";

	public DefRoot(IDefDefinition ... definitions) {
		this(ROOT,definitions);
	}
	public DefRoot(String name, IDefDefinition ... definitions) {
		super(name, definitions);
	}

	@Override
	public void inject(DefComponent parent) throws MException {
		throw new MException("can't link root into another container");
	}
	
	public DefRoot build() throws MException {
		super.inject(null);
		return this;
	}
	
}
