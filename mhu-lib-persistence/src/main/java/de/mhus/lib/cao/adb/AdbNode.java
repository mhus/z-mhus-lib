package de.mhus.lib.cao.adb;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.adb.AdbNodeData.TYPE;
import de.mhus.lib.cao.util.PropertiesNode;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class AdbNode extends PropertiesNode {

	private AdbNodeData data;

	public AdbNode(CaoConnection con, CaoNode parent, AdbNodeData data) {
		super(con, parent);
		this.data = data;
		init();
	}

	public AdbNode(CaoNode parent, AdbNodeData data) {
		super(parent);
		this.data = data;
		init();
	}
	
	private void init() {
		this.properties = data.getProperties();
		createMetadataByProperties();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isNode() {
		return data.getType() == TYPE.NODE;
	}

	@Override
	public void reload() throws CaoException {
		try {
			data.reload();
		} catch (MException e) {
			throw new CaoException(e);
		}
	}

	@Override
	public boolean isValid() {
		try {
			return data != null && !data.isAdbChanged();
		} catch (MException e) {
			log().d(this,e);
		}
		return false;
	}

	@Override
	public ResourceNode getNode(String key) {
		try {
			AdbNodeData childData = ((AdbConnection)getConnection()).getChild(data.getId(), key);
			if (childData != null) return new AdbNode(this, childData);
		} catch (MException e) {
			log().d(e);
		}
		return null;
	}

	@Override
	public ResourceNode[] getNodes() {
		try {
			AdbNodeData[] childData = ((AdbConnection)getConnection()).getChildren(data.getId());
			ResourceNode[] out = new ResourceNode[childData.length];
			for (int i = 0; i < childData.length; i++)
				out[i] = new AdbNode(this, childData[i]);
			return out;
		} catch (MException e) {
			log().d(e);
		}
		return new ResourceNode[0];
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		try {
			AdbNodeData[] childData = ((AdbConnection)getConnection()).getChildren(data.getId(), key);
			ResourceNode[] out = new ResourceNode[childData.length];
			for (int i = 0; i < childData.length; i++)
				out[i] = new AdbNode(this, childData[i]);
			return out;
		} catch (MException e) {
			log().d(e);
		}
		return new ResourceNode[0];
	}

	@Override
	public String[] getNodeKeys() {
		ResourceNode[] nodes = getNodes();
		String[] out = new String[nodes.length];
		try {
			for (int i = 0; i < nodes.length; i++)
				out[i] = nodes[i].getName();
		} catch (MException e) {
			log().d(e);
			out = new String[0];
		}
		return out;
	}

	@Override
	public InputStream getInputStream(String rendition) {
		return null;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public boolean hasContent() {
		return false;
	}

	@Override
	protected void doUpdate(MProperties modified) throws CaoException {
		data.setProperties(modified);
		try {
			data.save();
		} catch (MException e) {
			throw new CaoException(e);
		}
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public CaoNode getParent() {
		CaoNode p = super.getParent();
		if (p == null && data != null && data.getParent() != null) {
			p = ((AdbConnection)getConnection()).getResourceById(data.getParent().toString());
		}
		return p;
	}

	@Override
	public String getVersionLabel() throws MException {
		throw new NotSupportedException();
	}

	@Override
	public Set<String> getVersions() {
		throw new NotSupportedException();
	}

	@Override
	public CaoNode getVersion(String version) {
		throw new NotSupportedException();
	}

}
