package de.mhus.lib.core.util;


public abstract class CompareDirEntry {

	public abstract boolean isFolder();
	
	public String getParentPath( String path ) {
		return path.substring( 0, path.lastIndexOf( '/' ) ) + ',';
	}
	
	public String extractPath(String path) {
		return path.substring( 0, path.lastIndexOf( ',' ) );
	}
	
	/**
	 * Compare with the other entry. If the entries are not differend (not changed)
	 * return true. Otherwise return false. Usually compare size, MD5 and/or modify date.
	 * 
	 * @param other
	 * @return
	 */
	public abstract boolean compareWithEntry(CompareDirEntry other);
	
}
