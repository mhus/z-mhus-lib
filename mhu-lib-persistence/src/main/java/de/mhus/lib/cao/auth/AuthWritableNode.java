package de.mhus.lib.cao.auth;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoActionStarter;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class AuthWritableNode extends CaoWritableElement {

	private static final long serialVersionUID = 1L;
	private CaoWritableElement instance;
	private CaoNode readable;

	public AuthWritableNode(AuthNode parent, CaoNode readable, CaoWritableElement writableNode) {
		super(parent.getConnection(), parent);
		this.instance = writableNode;
		this.readable = readable;
	}

	@Override
	public CaoActionStarter getUpdateAction() throws MException {
		if (!((AuthConnection)con).hasWriteAccess(readable)) return null;
		return new AuthActionStarter(instance.getUpdateAction());
	}

	@Override
	public CaoWritableElement getWritableNode() throws MException {
		return this;
	}

	@Override
	public CaoMetadata getMetadata() {
		return instance.getMetadata();
	}

	@Override
	public String getId() throws MException {
		return instance.getId();
	}

	@Override
	public String getName() throws MException {
		return instance.getName();
	}

	@Override
	public boolean isNode() {
		return instance.isNode();
	}

	@Override
	public void reload() throws MException {
		instance.reload();
	}

	@Override
	public boolean isValid() {
		return instance.isValid();
	}

	@Override
	public Collection<String> getPropertyKeys() {
		return instance.getPropertyKeys();
	}

	@Override
	public CaoNode getNode(String key) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<CaoNode> getNodes() {
		throw new NotSupportedException();
	}

	@Override
	public Collection<CaoNode> getNodes(String key) {
		throw new NotSupportedException();
	}

	@Override
	public Collection<String> getNodeKeys() {
		throw new NotSupportedException();
	}

	@Override
	public InputStream getInputStream(String rendition) {
		throw new NotSupportedException();
	}

	@Override
	public URL getUrl() {
		return instance.getUrl();
	}

	@Override
	public boolean hasContent() {
		return instance.hasContent();
	}

	@Override
	public Object getProperty(String name) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return null;
		return instance.getProperty(name);
	}

	@Override
	public boolean isProperty(String name) {
		if (!((AuthConnection)con).hasReadAccess(instance, name)) return false;
		return instance.isProperty(name);
	}

	@Override
	public void removeProperty(String key) {
		if (!((AuthConnection)con).hasWriteAccess(instance, key)) return;
		instance.removeProperty(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if (!((AuthConnection)con).hasWriteAccess(instance, key)) return;
		instance.setProperty(key, value);
	}

	@Override
	public Collection<String> getRenditions() {
		throw new NotSupportedException();
	}

	@Override
	public void clear() {
		for (String key : keys())
			removeProperty(key);
	}

}
