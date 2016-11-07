package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class EmptyResourceNode<E extends ResourceNode<?>> extends ResourceNode<E> {

	private static final long serialVersionUID = 1L;
	private String name;

	public EmptyResourceNode() {}
	
	public EmptyResourceNode(String name) {
		this.name = name;
	}
	
	@Override
	public List<String> getPropertyKeys() {
		return MCollection.getEmptyList();
	}

	@Override
	public E getNode(String key) {
		return null;
	}

	@Override
	public List<E> getNodes() {
		return MCollection.getEmptyList();
	}

	@Override
	public List<E> getNodes(String key) {
		return MCollection.getEmptyList();
	}

	@Override
	public List<String> getNodeKeys() {
		return MCollection.getEmptyList();
	}

	@Override
	public String getName() throws MException {
		return name;
	}

	@Override
	public InputStream getInputStream(String key) {
		return null;
	}

	@Override
	public ResourceNode<?> getParent() {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public boolean hasContent() {
		return false;
	}

	@Override
	public Object getProperty(String name) {
		return null;
	}

	@Override
	public boolean isProperty(String name) {
		return false;
	}

	@Override
	public void removeProperty(String key) {
	}

	@Override
	public void setProperty(String key, Object value) {
		throw new NotSupportedException("empty resource");
	}

	@Override
	public boolean isEditable() {
		return false;
	}
	
	@Override
	public Collection<String> getRenditions() {
		return null;
	}

	@Override
	public void clear() {
	}


}
