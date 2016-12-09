package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

public class DefRoot extends DefComponent {

	private static final long serialVersionUID = 1L;
	public static final String ROOT = "root";
	private boolean build = false;

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
	
	public synchronized DefRoot build() throws MException {
		if (build) return this;
		build = true;
		super.inject(null);
		return this;
	}
	
	public boolean isBuild() {
		return build;
	}
	
}
