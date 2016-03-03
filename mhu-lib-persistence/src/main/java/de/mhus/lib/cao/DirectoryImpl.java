package de.mhus.lib.cao;

/**
 * <p>DirectoryImpl class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DirectoryImpl extends CaoDirectory {

	/**
	 * <p>register.</p>
	 *
	 * @param scheme a {@link java.lang.String} object.
	 * @param con a {@link de.mhus.lib.cao.CaoConnection} object.
	 */
	public void register(String scheme, CaoConnection con) {
		schemes.put(scheme, con);
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param scheme a {@link java.lang.String} object.
	 */
	public void unregister(String scheme) {
		schemes.remove(scheme);
	}

}
