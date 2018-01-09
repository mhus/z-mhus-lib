package de.mhus.lib.cao;

import java.util.LinkedList;

import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.errors.MException;

public class CaoPrincipal extends CaoPolicy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "name";
	public static final String PRINCIPAL_TYPE = "principal_type";
	public static enum PRINCIPAL_TYPES {USER,GROUP,ROLE,OTHER};

	private String name;
	protected int principalType;

	public CaoPrincipal(CaoCore core, CaoNode element, String name, PRINCIPAL_TYPES type, boolean readable, boolean writable)
			throws MException {
		super(core, element, readable, writable);
		this.name = name;
		principalType = type.ordinal();
	}

	@Override
	protected void fillMetaData(LinkedList<CaoMetaDefinition> definition) {
		definition.add(new CaoMetaDefinition(meta,NAME,TYPE.STRING,null,256) );
		definition.add(new CaoMetaDefinition(meta,PRINCIPAL_TYPE,TYPE.LONG,null,0) );
	}

	@Override
	public String getName() {
		return name;
	}

	public PRINCIPAL_TYPES getPrincipalType() {
		try {
			long index = getLong(PRINCIPAL_TYPE, PRINCIPAL_TYPES.OTHER.ordinal());
			if (index < 0 || index >+ PRINCIPAL_TYPES.values().length) return PRINCIPAL_TYPES.OTHER;
			return PRINCIPAL_TYPES.values()[(int)index];
		} catch (Exception e) {
			return PRINCIPAL_TYPES.OTHER;
		}
	}

	@Override
	public String getProperty(String name) {
		if (NAME.equals(name))
			return this.name;
		if (PRINCIPAL_TYPE.equals(name))
			return String.valueOf(principalType);
		return super.getString(name, null);
	}

}
