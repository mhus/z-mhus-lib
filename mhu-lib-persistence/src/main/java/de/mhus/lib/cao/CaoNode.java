package de.mhus.lib.cao;

import java.util.Collection;
import java.util.Set;

import de.mhus.lib.basics.Named;
import de.mhus.lib.cao.auth.AuthCore;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.Adaptable;
import de.mhus.lib.errors.MException;

public abstract class CaoNode extends ResourceNode<CaoNode> implements Adaptable<CaoAspect>, Named {

	private static final long serialVersionUID = 1L;
	protected CaoCore core;
	protected CaoNode parent;

	public CaoNode(CaoCore con, CaoNode parent) {
		this.core = con;
		this.parent = parent;
	}

	public CaoConnection getConnection() {
		return core.getConnection();
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
	 * Returns the unique id of this object for this connection. If the
	 * object is not unique, it returns null. For joining selects the
	 * unique id could not be returned.
	 * 
	 * @return Unique ID or null
	 * @throws CaoException
	 */
	public abstract String getId();

	/**
	 * Returns the id of the parent even if you have not access to the parent node you
	 * will get the pratent's ident.
	 * 
	 * @return Parent id or null if root
	 */
	public String getParentId() {
		CaoNode p = getParent();
		if (p == null) return null;
		return p.getId();
	}
	
	/**
	 * Return a display name of this object.
	 * 
	 * @return The display name
	 */
	@Override
	public abstract String getName();

	/**
	 * return true if this node can have children. If the node is only a
	 * leaf, it will return false.
	 * 
	 * @return x
	 */
	public abstract boolean isNode();

	/**
	 * Return the main path of the node.
	 * 
	 * @return
	 */
	public abstract String getPath();

	/**
	 * Return all pathes of the node.
	 * 
	 * @return
	 */
	public abstract Collection<String> getPaths();
	
	/**
	 * If the object is changed but not written this will recover the
	 * original state of the object. It will also load the new state from the
	 * system is something was changed from another process. After reload the
	 * object is no more dirty.
	 * @throws CaoException 
	 * @throws MException 
	 */
	public abstract void reload() throws MException;

	@Override
	public String toString() {
		return getName() + " ("+getId()+")";
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

	@Override
	@SuppressWarnings("unchecked")
	public <T extends CaoAspect> T adaptTo(Class<? extends CaoAspect> ifc) {
		CaoAspectFactory<? extends CaoAspect> factory = getConnection().getAspectFactory(ifc);
		if (factory == null) return null;
		return (T) factory.getAspectFor(this);
	}
	
}
