package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>DirConfig class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DirConfig extends PropertiesConfig {

	private File dir;

	/**
	 * <p>Constructor for DirConfig.</p>
	 *
	 * @param dir a {@link java.io.File} object.
	 */
	public DirConfig(File dir) {
		this.dir = dir;
		File f = new File(dir,"_.properties");
		if (f.exists() && f.isFile()) {
			try {
				FileReader r = new FileReader(f);
				readConfig(r);
				r.close();
			} catch (Exception e) {
				log().d(dir,e);
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getPropertyKeys() {
		return new String[0];
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode getNode(String key) {
		try {
			String keyDot = key + ".";
			for ( File f : dir.listFiles())
				if (f.isDirectory() && f.getName().equals(key) ||
					!f.isDirectory() && f.getName().startsWith(keyDot))
					return MSingleton.baseLookup(this,MConfigFactory.class).createConfigFor(f);
		} catch (Throwable e) {
			
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode[] getNodes(String key) {
		LinkedList<ResourceNode> out = new LinkedList<ResourceNode>();
			String keyDot = key + ".";
			for ( File f : dir.listFiles())
				try {
					if (f.isDirectory() && f.getName().equals(key) ||
						!f.isDirectory() && f.getName().startsWith(keyDot))
						out.add(MSingleton.baseLookup(this,MConfigFactory.class).createConfigFor(f));
				} catch (Throwable e) {
					
				}
		
		return out.toArray(new WritableResourceNode[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode[] getNodes() {
		LinkedList<WritableResourceNode> out = new LinkedList<WritableResourceNode>();
			for ( File f : dir.listFiles())
				try {
					if (f.isDirectory() && !f.getName().startsWith(".") && !f.isHidden())
						out.add(MSingleton.baseLookup(this,MConfigFactory.class).createConfigFor(f));
					else
					if (!f.isDirectory() && !f.isHidden()) {
						WritableResourceNode conf = MSingleton.baseLookup(this,MConfigFactory.class).createConfigFor(f);
						if (conf != null)
							out.add(conf);
					}
				} catch (Throwable e) {
					
				}
		
		return out.toArray(new WritableResourceNode[out.size()]);
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getNodeKeys() {
		LinkedList<String> out = new LinkedList<String>();
		
		for ( File f : dir.listFiles()) {
			if (f.isDirectory() && !out.contains(f.getName()))
				out.add(f.getName());
			else
			if (!f.isDirectory() && !f.getName().startsWith("_")) {
				String name = f.getName();
				int pos = name.indexOf('.');
				if (pos > 0)
					name = name.substring(0,pos);
				if (!out.contains(name))
					out.add(name);
			}
		}
		
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return dir.getName();
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode createConfig(String key) throws MException {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public int moveConfig(ResourceNode config, int newPos) throws MException {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public void removeConfig(ResourceNode config) throws MException {
	}

	/** {@inheritDoc} */
	@Override
	public WritableResourceNode getParent() {
		File parent = dir.getParentFile();
		if (parent == null) return null;
		return new DirConfig(parent);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * <p>getDirectory.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	public File getDirectory() {
		return dir;
	}

}
