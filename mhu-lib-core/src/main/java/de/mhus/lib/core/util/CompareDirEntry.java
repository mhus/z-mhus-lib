package de.mhus.lib.core.util;


/**
 * <p>Abstract CompareDirEntry class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CompareDirEntry {

	/**
	 * <p>isFolder.</p>
	 *
	 * @return a boolean.
	 */
	public abstract boolean isFolder();
	
	/**
	 * <p>getParentPath.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getParentPath( String path ) {
		return path.substring( 0, path.lastIndexOf( '/' ) ) + ',';
	}
	
	/**
	 * <p>extractPath.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String extractPath(String path) {
		return path.substring( 0, path.lastIndexOf( ',' ) );
	}
	
	/**
	 * Compare with the other entry. If the entries are not differend (not changed)
	 * return true. Otherwise return false. Usually compare size, MD5 and/or modify date.
	 *
	 * @param other a {@link de.mhus.lib.core.util.CompareDirEntry} object.
	 * @return a boolean.
	 */
	public abstract boolean compareWithEntry(CompareDirEntry other);
	
}
