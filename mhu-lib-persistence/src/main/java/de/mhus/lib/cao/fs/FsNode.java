package de.mhus.lib.cao.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import de.mhus.lib.cao.util.PropertiesNode;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.AccessDeniedException;

public class FsNode extends PropertiesNode {

	private static final long serialVersionUID = 1L;
	private File file;

	public FsNode(FsConnection connection, File file) {
		super(connection, null);
		this.file = file;
		reload();
		this.metadata = ((FsConnection)getConnection()).getMetadata();
		this.policyProvider = ((FsConnection)getConnection()).getPolicyProvider();
	}

	@Override
	public boolean isNode() {
		return file.isDirectory();
	}

	@Override
	public void reload() {
		((FsConnection)getConnection()).fillProperties(file, properties);
		this.id = file.getPath();
		this.name = file.getName();
	}

	@Override
	public boolean isValid() {
		return file.exists();
	}

	@Override
	public ResourceNode getNode(String key) {
		File f = new File(file, key);
		if (f.exists())
			return new FsNode((FsConnection) getConnection(), f);
		return null;
	}

	@Override
	public ResourceNode[] getNodes() {
		LinkedList<ResourceNode> out = new LinkedList<>();
		for (File f : file.listFiles()) {
			if (f.isHidden() || f.getName().startsWith(".")) continue;
			out.add( new FsNode((FsConnection) getConnection(), f));
		}
		return out.toArray(new ResourceNode[ out.size() ]);
	}

	@Override
	public ResourceNode[] getNodes(String key) {
		ResourceNode out = getNode(key);
		if (out == null)
			return new ResourceNode[0];
		return new ResourceNode[] {out};
	}

	@Override
	public String[] getNodeKeys() {
		LinkedList<String> out = new LinkedList<>();
		for (File f : file.listFiles()) {
			if (f.isHidden() || f.getName().startsWith(".")) continue;
			out.add( f.getName() );
		}
		return out.toArray(new String[ out.size() ]);
	}

	@Override
	public InputStream getInputStream(String rendition) {
		
		File contentFile = ((FsConnection)getConnection()).getContentFileFor(file, rendition);
		if (contentFile == null || !contentFile.exists()) return null;
		try {
			return new FileInputStream(contentFile);
		} catch (FileNotFoundException e) {
			log().d(contentFile,e);
		}
		return null;
		
	}

	@Override
	public URL getUrl() {
		File contentFile = ((FsConnection)getConnection()).getContentFileFor(file, null);
		if (contentFile == null || !contentFile.exists()) return null;
		try {
			return contentFile.toURL();
		} catch (MalformedURLException e) {
			log().d(file,e);
		}
		return null;
	}

	@Override
	public boolean hasContent() {
		return file.isFile() || ((FsConnection)getConnection()).isUseMetaFile();
	}

	@Override
	protected void doUpdate(MProperties modified) {
		if (!isEditable()) throw new AccessDeniedException(file);
		File metaFile = ((FsConnection)getConnection()).getMetaFileFor(file);
		try {
			properties.save(metaFile);
		} catch (IOException e) {
			log().w(metaFile,e);
		}
	}

	@Override
	public boolean isEditable() {
		return ((FsConnection)getConnection()).isUseMetaFile() && file.canWrite() && file.getParentFile().canWrite();
	}

}
