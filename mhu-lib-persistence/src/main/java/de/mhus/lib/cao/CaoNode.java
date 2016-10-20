package de.mhus.lib.cao;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public abstract class CaoNode extends ResourceNode {

	private static final long serialVersionUID = 1L;
	private CaoConnection con;
	private CaoNode parent;
	protected CaoPolicyProvider policyProvider;

	public CaoNode(CaoNode parent) {
		this(parent.getConnection(),parent);
	}

	public CaoNode(CaoConnection con, CaoNode parent) {
		this.con = con;
		this.parent = parent;
	}

	public CaoConnection getConnection() {
		return con;
	}

	/**
	 * Returns a parent element where this element is coming from. Usually this
	 * is possible if the element is created from a tree node. The element can
	 * also have other parents. If no parent is known in this situation the
	 * function returns null.
	 * 
	 * @return The parent in this situation or null
	 */
	@Override
	public CaoNode getParent() {
		return parent;
	}

	/**
	 * 
	 * @return A writable element
	 * @throws MException
	 */
	public abstract CaoWritableElement getWritableNode() throws MException;

	/**
	 * Returns the meta structure definition for this data.
	 * 
	 * @return The meta structure definition
	 */
	public abstract CaoMetadata getMetadata();

	/**
	 * Returns the unique id of this object for this connection. If the
	 * object is not unique, it returns null. For joining selects the
	 * unique id could not be returned.
	 * 
	 * @return Unique ID or null
	 * @throws CaoException
	 */
	public abstract String getId() throws CaoException;

	/**
	 * Return a display name of this object.
	 * 
	 * @return The display name
	 * @throws MException
	 */
	@Override
	public abstract String getName() throws MException;

	/**
	 * return true if this node can have children. If the node is only a
	 * leaf, it will return false.
	 * 
	 * @return x
	 */
	public abstract boolean isNode();

	/**
	 * If the object is changed but not written this will recover the
	 * original state of the object. It will also load the new state from the
	 * system is something was changed from another process. After reload the
	 * object is no more dirty.
	 * @throws CaoException 
	 */
	public abstract void reload() throws CaoException;

	@Override
	public String toString() {
		try {
			return getName() + " ("+getId()+")";
		} catch (MException e) {
		}
		return super.toString();
	}

	/**
	 * Returns true if the element is valid, invalid means e.g. the corresponding
	 * element has been deleted. This method returns not false if the element data is changed. Only
	 * CaoWritable can be changed and the have a isDirty() method.
	 * 
	 * @return x
	 */
	@Override
	public abstract boolean isValid();

	@Override
	public boolean equals(Object other) {
		if (other instanceof CaoNode) {
			try {
				return ((CaoNode)other).getConnection().equals(getConnection()) && ((CaoNode)other).getId().equals(getId());
			} catch (Throwable t) {}
		}
		return super.equals(other);
	}

	/**
	 * The feature is overwritten from IPropertie. The CaoElement is not editable. Use the writableElement to change
	 * the data.
	 */
	@Override
	public boolean isEditable() {
		return false;
	}

	/**
	 * Return the current access policy for this element.
	 * 
	 * @return x
	 * @throws CaoException
	 */
	public CaoPolicy getAccessPolicy() throws CaoException {
		if (policyProvider == null) return null;
		return policyProvider.getAccessPolicy(this);
	}

}
