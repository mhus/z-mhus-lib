package de.mhus.lib.core.directory;

import java.io.InputStream;
import java.net.URL;

import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class EmptyResourceNode extends ResourceNode {

	private String name;

	public EmptyResourceNode() {}
	
	public EmptyResourceNode(String name) {
		this.name = name;
	}
	
	@Override
	public String[] getPropertyKeys() {
		return new String[0];
	}

	@Override
	public ResourceNode getNode(String key) {
		return null;
	}

	@Override
	public ResourceNode[] getNodes() {
		return new ResourceNode[0];
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		return new ResourceNode[0];
	}

	@Override
	public String[] getNodeKeys() {
		return new String[0];
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
	public ResourceNode getParent() {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public boolean isValide() {
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

}
