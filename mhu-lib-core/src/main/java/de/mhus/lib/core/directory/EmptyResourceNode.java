package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;

import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>EmptyResourceNode class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class EmptyResourceNode extends ResourceNode {

	private String name;

	/**
	 * <p>Constructor for EmptyResourceNode.</p>
	 */
	public EmptyResourceNode() {}
	
	/**
	 * <p>Constructor for EmptyResourceNode.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public EmptyResourceNode(String name) {
		this.name = name;
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
	public String getName() throws MException {
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getParent() {
		return null;
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
		throw new NotSupportedException("empty resource");
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

}
