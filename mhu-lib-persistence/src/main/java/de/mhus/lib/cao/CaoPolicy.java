package de.mhus.lib.cao;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Set;

import de.mhus.lib.cao.CaoMetaDefinition.TYPE;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>CaoPolicy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoPolicy extends CaoNode {

	/** Constant <code>READ="read"</code> */
	public static final String READ = "read";
	/** Constant <code>WRITE="write"</code> */
	public static final String WRITE = "write";
	/** Constant <code>POLICIES="policies"</code> */
	public static final String POLICIES = "policies";
	/** Constant <code>PRINCIPAL="principal"</code> */
	public static final String PRINCIPAL = "principal";

	/** Constant <code>CATEGORY_POLICY="policy"</code> */
	public static final String CATEGORY_POLICY = "policy";
	/** Constant <code>CATEGORY_RIGHT="right"</code> */
	public static final String CATEGORY_RIGHT = "right";

	protected boolean writable;
	protected boolean readable;
	protected CaoNode element;
	protected CaoPolicy proxy = null;
	protected CaoMetadata meta;

	/**
	 * <p>Constructor for CaoPolicy.</p>
	 *
	 * @param element a {@link de.mhus.lib.cao.CaoNode} object.
	 * @param readable a boolean.
	 * @param writable a boolean.
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public CaoPolicy(CaoNode element, boolean readable, boolean writable) throws CaoException {
		super(element);
		this.element = element;
		this.readable = readable;
		this.writable = writable;
		meta = new CaoMetadata(getConnection().getDriver());
		fillMetaData(meta.definition);
	}

	/**
	 * Overwrite this function to append or set your own right definitions.
	 *
	 * @param definition a {@link java.util.LinkedList} object.
	 */
	protected void fillMetaData(LinkedList<CaoMetaDefinition> definition) {
		definition.add(new CaoMetaDefinition(meta,READ,TYPE.BOOLEAN,null,0,CATEGORY_RIGHT) );
		definition.add(new CaoMetaDefinition(meta,WRITE,TYPE.BOOLEAN,null,0,CATEGORY_RIGHT) );
		definition.add(new CaoMetaDefinition(meta,POLICIES,TYPE.LIST,null,0,CATEGORY_POLICY) );
		definition.add(new CaoMetaDefinition(meta,PRINCIPAL,TYPE.ELEMENT,null,0) );
	}

	/**
	 * <p>isWritable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isWritable() {
		if (proxy != null) return proxy.isWritable();
		return getBoolean(WRITE, writable);
	}

	/**
	 * <p>isReadable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isReadable() {
		if (proxy != null) return proxy.isReadable();
		return getBoolean(READ, readable);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNode() {
		if (proxy != null) return proxy.isNode();
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String getId() throws CaoException {
		if (proxy != null) return proxy.getId();
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() throws MException {
		if (proxy != null) return proxy.getName();
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public CaoMetadata getMetadata() {
		if (proxy != null) return proxy.getMetadata();
		return meta;
	}

	/** {@inheritDoc} */
	@Override
	public CaoNode getParent() {
		if (proxy != null) return proxy.getParent();
		return null;
	}

	//	@Override
	//	public String getString(String name) throws CaoException {
	//		if (proxy != null) return proxy.getString(name);
	//		if (name.equals(PRINCIPAL)) {
	//			return getConnection().getCurrentUser().getName();
	//		}
	//		return MCast.toString(getBoolean(name, false));
	//	}

	/** {@inheritDoc} */
	@Override
	public boolean getBoolean(String name, boolean def) {
		if (proxy != null) return proxy.getBoolean(name, def);
		if (READ.equals(name))  return readable;
		if (WRITE.equals(name)) return writable;
		return def;
	}

	//	@Override
	//	public CaoList getList(String name, CaoAccess access, String... attributes)
	//			throws CaoException {
	//		if (proxy != null) return proxy.getList(name, access, attributes);
	//
	//		if (POLICIES.equals(name)) {
	//			return getPoliciesList(attributes);
	//		}
	//
	//		throw new CaoNotFoundException(this,"list",name);
	//	}

	/**
	 * Overwrite this function to return another policy informations.
	 *
	 * @param attributes an array of {@link java.lang.String} objects.
	 * @throws de.mhus.lib.cao.CaoException if any.
	 * @return a {@link de.mhus.lib.cao.CaoList} object.
	 */
	protected CaoList getPoliciesList(String[] attributes) throws CaoException {
		// Returns a list with the current user
		CaoList list = new CaoList(this);
		list.add(this);
		return list;
	}

	//	@Override
	//	public Object getObject(String name, String... attributes)
	//			throws CaoException {
	//		if (proxy != null) return proxy.getObject(name, attributes);
	//
	//		if (name.equals(PRINCIPAL)) {
	//			return getConnection().getCurrentUser();
	//		}
	//
	//		throw new CaoNotSupportedException();
	//	}

	/** {@inheritDoc} */
	@Override
	public CaoWritableElement getWritableNode() throws MException {
		if (proxy != null) return proxy.getWritableNode();
		throw new CaoNotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public void reload() throws CaoException {
		proxy = element.getAccessPolicy();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValid() {
		return element.isValid();
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return element.keys();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		return new String[0];
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getNode(String key) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes() {
		return new ResourceNode[0];
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes(String key) {
		return new ResourceNode[0];
	}

	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		return new String[0];
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {

	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {

	}

	/** {@inheritDoc} */
	@Override
	public URL getUrl() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValide() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasContent() {
		return false;
	}

}
