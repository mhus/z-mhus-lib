package de.mhus.lib.core.directory.fs;

import java.io.File;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>FileResourceRoot class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FileResourceRoot extends FileResource {

	/**
	 * <p>Constructor for FileResourceRoot.</p>
	 *
	 * @param documentRoot a {@link java.io.File} object.
	 */
	public FileResourceRoot(File documentRoot) {
		super(null, null, documentRoot);
	}

	/**
	 * <p>getResource.</p>
	 *
	 * @param target a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getResource(String target) {
		
		return getResource(this,target);
	}

	private ResourceNode getResource(FileResource parent,
			String target) {
		if (parent == null || target == null) return null;
		if (target.length() == 0) return parent;
		
		String next = null;
		if (MString.isIndex(target, '/')) {
			next = MString.beforeIndex(target, '/');
			target = MString.afterIndex(target, '/');
		} else {
			next = target;
			target = "";
		}
		if (next.length() == 0) return getResource(parent,target);
		
		ResourceNode n = parent.getNode(next);
		if (n == null) return null;
		
		return getResource((FileResource) n, target);
	}

}
