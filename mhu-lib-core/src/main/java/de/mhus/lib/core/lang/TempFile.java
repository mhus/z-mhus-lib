package de.mhus.lib.core.lang;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import de.mhus.lib.core.MSystem;

/**
 * The file will be deleted if the reference of the object is lost and the gc remove the object
 * from the vm.
 * 
 * @author mikehummel
 *
 */
public class TempFile extends File {

	private static final long serialVersionUID = 1L;
	
	public TempFile(File clone) {
		super(clone.getAbsolutePath());
		deleteOnExit();
	}
	
	public TempFile(File parent, String child) {
		super(parent, child);
		deleteOnExit();
	}

	public TempFile(String parent, String child) {
		super(parent, child);
		deleteOnExit();
	}

	public TempFile(String pathname) {
		super(pathname);
		deleteOnExit();
	}

	public TempFile(URI uri) {
		super(uri);
		deleteOnExit();
	}

	@Override
	protected void finalize() throws Throwable {
		delete(); // delete the tmp file if reference is lost
	}

	public static File createTempFile(String prefix, String suffix) throws IOException {
		File tmpFile = File.createTempFile(prefix, suffix);
		return new TempFile(tmpFile);
	}
	
}
