package de.mhus.lib.core.directory.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>FileResource class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FileResource extends ResourceNode {

	public enum KEYS {NAME, LENGTH, MODIFIED, TYPE, HIDDEN};
	public enum TYPE {FILE,DIRCTORY,UNKNOWN}
	
	/** Constant <code>UNKNOWN_LENGTH=-1</code> */
	public static final long UNKNOWN_LENGTH = -1;
	
	private FileResourceRoot root;
	private File file;
	private FileResource parent;
	private HashMap<String,FileResource> cache = new HashMap<>();

	/**
	 * <p>Constructor for FileResource.</p>
	 *
	 * @param root a {@link de.mhus.lib.core.directory.fs.FileResourceRoot} object.
	 * @param parent a {@link de.mhus.lib.core.directory.fs.FileResource} object.
	 * @param file a {@link java.io.File} object.
	 */
	public FileResource(FileResourceRoot root, FileResource parent, File file) {
		if (root == null) root = (FileResourceRoot) this;
		this.root = root;
		this.file = file;
		this.parent = parent;
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		
		KEYS[] v = KEYS.values();
		String[] out = new String[v.length];
		for (int i = 0; i < v.length; i++)
			out[i] = v[i].name().toLowerCase();
		
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getNode(String key) {
		if (key == null) return null;
		if (key.equals("..") || key.equals(".")) return null;
		if (key.indexOf('/') > -1 || key.indexOf('\\') > -1) return null; // only direct children
		// TODO special chars ?!!
		
		FileResource cached = cache.get(key);
		if (cached != null) {
			if (cached.isValide()) return cached;
			cache.remove(key);
		}
		File f = new File(file, key);
		if (!f.exists()) return null;
		cached = new FileResource(root, this, f);
		cache.put(key,cached);

		return cached;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes() {
		LinkedList<ResourceNode> out = new LinkedList<>();
		for (String sub : file.list()) {
			ResourceNode n = getNode(sub);
			if (n != null)
				out.add(n);
		}
		return out.toArray(new ResourceNode[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode[] getNodes(String key) {
		ResourceNode n = getNode(key);
		if (n == null) return new ResourceNode[0];
		return new ResourceNode[] {n};
	}

	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		LinkedList<String> out = new LinkedList<>();
		for (String sub : file.list()) {
			ResourceNode n = getNode(sub);
			if (n != null)
				out.add(sub);
		}
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public String getName() throws MException {
		return file.getName();
	}

	/** {@inheritDoc} */
	@Override
	public InputStream getInputStream(String key) {
		if (file.isDirectory()) return null;
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public ResourceNode getParent() {
		return parent;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public URL getUrl() {
		try {
			return file.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		
		if (name.equals("file"))
			return file;
		if (name.equals("filepath"))
			return file.getAbsolutePath();
		
		try {
			KEYS key = KEYS.valueOf(name.toUpperCase());
			switch (key) {
			case NAME:
				return file.getName();
			case LENGTH:
				return file.length();
			case MODIFIED:
				if (file.isFile())
					return file.lastModified();
				else
					return UNKNOWN_LENGTH;
			case TYPE:
				if (file.isFile())
					return TYPE.FILE;
				if (file.isDirectory())
					return TYPE.DIRCTORY;
				return TYPE.UNKNOWN;
			case HIDDEN:
				return file.isHidden();
			}
		} catch (IllegalArgumentException e) {}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		try {
			KEYS key = KEYS.valueOf(name.toUpperCase());
			return key != null;
		} catch (Throwable t) {
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValide() {
		return file != null && file.exists();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasContent() {
		return file.isFile();
	}

}
