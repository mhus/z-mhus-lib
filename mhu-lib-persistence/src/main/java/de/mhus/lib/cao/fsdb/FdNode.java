package de.mhus.lib.cao.fsdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.util.PropertiesNode;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.util.EmptyList;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public class FdNode extends PropertiesNode {

	private static final long serialVersionUID = 1L;
	private File file;

	public FdNode(FdConnection connection, File file, FdNode parent) {
		super(connection, parent);
		this.file = file;
		reload();
		this.metadata = ((FdConnection)getConnection()).getMetadata();
	}

	@Override
	public boolean isNode() {
		return file.isDirectory();
	}

	@Override
	public void reload() {
		((FdConnection)getConnection()).fillProperties(file, properties);
//		FdNode root = ((FdNode)((FdConnection)getConnection()).getRoot());
		this.id = properties.getString("_id", "");
		this.name = file.getName();
	}

	File getFile() {
		return file;
	}

	@Override
	public boolean isValid() {
		return file.exists();
	}

	@Override
	public CaoNode getNode(String key) {
		File f = new File(file, key);
		if (f.exists())
			return new FdNode((FdConnection) getConnection(), f, this);
		return null;
	}

	@Override
	public Collection<CaoNode> getNodes() {
		LinkedList<CaoNode> out = new LinkedList<>();
		for (File f : file.listFiles()) {
			if (f.isHidden() || f.getName().startsWith(".") || f.getName().startsWith("__cao.")) continue;
			out.add( new FdNode((FdConnection) getConnection(), f, this));
		}
		return out;
	}

	@Override
	public Collection<CaoNode> getNodes(String key) {
		CaoNode out = getNode(key);
		LinkedList<CaoNode> list = new LinkedList<>();
		if (out != null)
			list.add(out);
		return list;
	}

	@Override
	public Collection<String> getNodeKeys() {
		LinkedList<String> out = new LinkedList<>();
		for (File f : file.listFiles()) {
			if (f.isHidden() || f.getName().startsWith(".")) continue;
			out.add( f.getName() );
		}
		return out;
	}

	@Override
	public InputStream getInputStream(String rendition) {
		
		File contentFile = ((FdConnection)getConnection()).getContentFileFor(file, rendition);
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
		File contentFile = ((FdConnection)getConnection()).getContentFileFor(file, null);
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
		return true;
	}

	@Override
	protected void doUpdate(MProperties modified) {
		if (!isEditable()) throw new AccessDeniedException(file);
		File metaFile = ((FdConnection)getConnection()).getMetaFileFor(file);
		modified.remove("id");
		try {
			modified.save(metaFile);
		} catch (IOException e) {
			log().w(metaFile,e);
		}
		reload();
	}

	@Override
	public boolean isEditable() {
		return file.canWrite() && file.getParentFile().canWrite();
	}

	@Override
	public Collection<String> getRenditions() {
		if (hasContent()) {
			// TODO if use meta collect list of renditions
			return new EmptyList<>();
		}
		return null;
	}

	@Override
	public void clear() {
	}

}