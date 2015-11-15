package de.mhus.lib.core.definition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsFactory;
import de.mhus.lib.errors.MException;

public class DefRoot extends DefComponent {

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
	
	@Override
	public DefRoot transform(IDefTransformer transformer)  throws MException {
		return (DefRoot) transformer.transform(this);
	}
}
