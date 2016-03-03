package de.mhus.lib.core.definition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsFactory;
import de.mhus.lib.errors.MException;

/**
 * <p>DefRoot class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefRoot extends DefComponent {

	/** Constant <code>ROOT="root"</code> */
	public static final String ROOT = "root";

	/**
	 * <p>Constructor for DefRoot.</p>
	 *
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public DefRoot(IDefDefinition ... definitions) {
		this(ROOT,definitions);
	}
	/**
	 * <p>Constructor for DefRoot.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public DefRoot(String name, IDefDefinition ... definitions) {
		super(name, definitions);
		setString("columns", "8");
	}

	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent parent) throws MException {
		throw new MException("can't link root into another container");
	}

	/**
	 * <p>createNls.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
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
	
	/**
	 * <p>build.</p>
	 *
	 * @return a {@link de.mhus.lib.core.definition.DefRoot} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public DefRoot build() throws MException {
		super.inject(null);
		return this;
	}
	
	/** {@inheritDoc} */
	@Override
	public DefRoot transform(IDefTransformer transformer)  throws MException {
		return (DefRoot) transformer.transform(this);
	}
}
