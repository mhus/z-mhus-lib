package de.mhus.lib.cao;

import java.util.LinkedList;

import de.mhus.lib.cao.CaoMetaDefinition.TYPE;

/**
 * <p>CaoPrincipal class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoPrincipal extends CaoPolicy {

	/** Constant <code>NAME="name"</code> */
	public static final String NAME = "name";
	/** Constant <code>PRINCIPAL_TYPE="principal_type"</code> */
	public static final String PRINCIPAL_TYPE = "principal_type";
	public static enum PRINCIPAL_TYPES {USER,GROUP,ROLE,OTHER};

	private String name;
	protected int principalType;

	/**
	 * <p>Constructor for CaoPrincipal.</p>
	 *
	 * @param element a {@link de.mhus.lib.cao.CaoNode} object.
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link de.mhus.lib.cao.CaoPrincipal.PRINCIPAL_TYPES} object.
	 * @param readable a boolean.
	 * @param writable a boolean.
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public CaoPrincipal(CaoNode element, String name, PRINCIPAL_TYPES type, boolean readable, boolean writable)
			throws CaoException {
		super(element, readable, writable);
		this.name = name;
		principalType = type.ordinal();
	}

	/** {@inheritDoc} */
	@Override
	protected void fillMetaData(LinkedList<CaoMetaDefinition> definition) {
		definition.add(new CaoMetaDefinition(meta,NAME,TYPE.STRING,null,256) );
		definition.add(new CaoMetaDefinition(meta,PRINCIPAL_TYPE,TYPE.LONG,null,0) );
	}

	/** {@inheritDoc} */
	@Override
	public String getName() throws CaoException {
		return name;
	}

	/**
	 * <p>Getter for the field <code>principalType</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoPrincipal.PRINCIPAL_TYPES} object.
	 */
	public PRINCIPAL_TYPES getPrincipalType() {
		try {
			long index = getLong(PRINCIPAL_TYPE, PRINCIPAL_TYPES.OTHER.ordinal());
			if (index < 0 || index >+ PRINCIPAL_TYPES.values().length) return PRINCIPAL_TYPES.OTHER;
			return PRINCIPAL_TYPES.values()[(int)index];
		} catch (Exception e) {
			return PRINCIPAL_TYPES.OTHER;
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getProperty(String name) {
		if (NAME.equals(name))
			return this.name;
		if (PRINCIPAL_TYPE.equals(name))
			return String.valueOf(principalType);
		return super.getString(name, null);
	}

}
