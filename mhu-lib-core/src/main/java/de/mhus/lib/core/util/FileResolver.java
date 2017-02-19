package de.mhus.lib.core.util;

import java.io.File;
import java.util.Set;

public interface FileResolver {

	File getFile(String path);
	
	/**
	 * Returns a list of pathes with the given path. Directories ends with slash.
	 * 
	 * @param path
	 * @return
	 */
	Set<String> getContent(String path);

	File getRoot();
	
}
