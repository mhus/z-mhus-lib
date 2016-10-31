package de.mhus.lib.cao.aaa;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoOperation;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.errors.MException;

public class AuthWritableNode extends CaoWritableElement {

	public AuthWritableNode(AuthNode authNode, CaoWritableElement writableNode) {
		super(authNode.getConnection(), writableNode); //TODO !!!
	}

	@Override
	public CaoOperation getUpdateOperation() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaoWritableElement getWritableNode() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaoMetadata getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reload() throws MException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getVersionLabel() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getVersions() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaoNode getVersion(String version) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getPropertyKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaoNode getNode(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CaoNode> getNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CaoNode> getNodes(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getNodeKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream(String rendition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isProperty(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeProperty(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProperty(String key, Object value) {
		// TODO Auto-generated method stub

	}

}
