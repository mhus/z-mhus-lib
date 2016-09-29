package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

/**
 * <p>DefRoot class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefRoot extends DefComponent {

	private static final long serialVersionUID = 1L;
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
	}

	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent parent) throws MException {
		throw new MException("can't link root into another container");
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
	
}
