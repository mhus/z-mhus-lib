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

	public DefRoot(IDefDefinition ... definitions) throws MException {
		this(ROOT,definitions);
	}
	public DefRoot(String name, IDefDefinition ... definitions) throws MException {
		super(name, definitions);
		setString("columns", "8");
	}

	@Override
	public void inject(DefComponent parent) throws MException {
		throw new MException("can't link root into another container");
	}

	public MNls createNls() throws MException {
		Properties p = new Properties();
		fillNls(p);
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		try {
			p.store(o, "");
			return base(MNlsFactory.class).load(new ByteArrayInputStream(o.toByteArray()));
		} catch (IOException e) {
			throw new MException(e);
		}
	}
	
	public DefRoot build() throws MException {
		super.inject(null);
		return this;
	}
	
	public DefRoot transform(IDefTransformer transformer)  throws MException {
		return (DefRoot) transformer.transform(this);
	}
}
