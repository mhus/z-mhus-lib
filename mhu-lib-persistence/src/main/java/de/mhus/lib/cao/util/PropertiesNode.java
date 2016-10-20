package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoUtil;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public abstract class PropertiesNode extends CaoNode {

	private static final long serialVersionUID = 1L;
	protected MProperties properties = new MProperties();
	protected CaoMetadata metadata;
	protected String id;
	protected String name;
	
	public PropertiesNode(CaoNode parent) {
		super(parent);
		metadata = new MutableMetadata(parent.getConnection().getDriver());
	}
	
	public PropertiesNode(CaoConnection con, CaoNode parent) {
		super(con, parent);
		metadata = new MutableMetadata(con.getDriver());
	}

	@Override
	public CaoWritableElement getWritableNode() throws MException {
		if (isEditable()) return new WritablePropertiesNode(this);
		return null;
	}

	@Override
	public CaoMetadata getMetadata() {
		return metadata;
	}

	@Override
	public String getId() throws CaoException {
		return id;
	}

	@Override
	public String getName() throws MException {
		return name;
	}

	@Override
	public String[] getPropertyKeys() {
		return properties.keySet().toArray(new String[0]);
	}

	@Override
	public Object getProperty(String name) {
		return properties.getProperty(name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		throw new NotSupportedException();
	}

	@Override
	public void setProperty(String key, Object value) {
		throw new NotSupportedException();
	}

	protected void createMetadataByProperties() {
		for (java.util.Map.Entry<String, Object> entry : properties.entrySet()) {
			((MutableMetadata)metadata).addDefinition(entry.getKey(), CaoUtil.objectToMetaType(entry.getValue()), 700);
		}
	}

	protected abstract void doUpdate(MProperties modified) throws CaoException;
	
}
