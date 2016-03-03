package de.mhus.lib.cao;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>Abstract CaoNode class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoNode extends ResourceNode {

	private CaoConnection con;
	private CaoNode parent;

	/**
	 * <p>Constructor for CaoNode.</p>
	 *
	 * @param parent a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoNode(CaoNode parent) {
		this(parent.getConnection(),parent);
	}

	/**
	 * <p>Constructor for CaoNode.</p>
	 *
	 * @param con a {@link de.mhus.lib.cao.CaoConnection} object.
	 * @param parent a {@link de.mhus.lib.cao.CaoNode} object.
	 */
	public CaoNode(CaoConnection con, CaoNode parent) {
		this.con = con;
		this.parent = parent;
	}

	/**
	 * <p>getConnection.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 */
	public CaoConnection getConnection() {
		return con;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns a parent element where this element is coming from. Usually this
	 * is possible if the element is created from a tree node. The element can
	 * also have other parents. If no parent is known in this situation the
	 * function returns null.
	 */
	@Override
	public CaoNode getParent() {
		return parent;
	}

	/**
	 * <p>getWritableNode.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoWritableElement} object.
	 * @throws de.mhus.lib.errors.MException if any.
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
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public abstract String getId() throws CaoException;

	/**
	 * {@inheritDoc}
	 *
	 * Return a display name of this object.
	 */
	@Override
	public abstract String getName() throws MException;

	/**
	 * return true if this node can have children. If the node is only a
	 * leaf, it will return false.
	 *
	 * @return a boolean.
	 */
	public abstract boolean isNode();

	/**
	 * If the object is changed but not written this will recover the
	 * original state of the object. It will also load the new state from the
	 * system is something was changed from another process. After reload the
	 * object is no more dirty.
	 *
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public abstract void reload() throws CaoException;

	/** {@inheritDoc} */
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
	 * @return a boolean.
	 */
	public abstract boolean isValid();

	/** {@inheritDoc} */
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
	 * {@inheritDoc}
	 *
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
	 * @throws de.mhus.lib.cao.CaoException if any.
	 * @return a {@link de.mhus.lib.cao.CaoPolicy} object.
	 */
	public CaoPolicy getAccessPolicy() throws CaoException {
		return new CaoPolicy(this ,true, isEditable());
	}

}
