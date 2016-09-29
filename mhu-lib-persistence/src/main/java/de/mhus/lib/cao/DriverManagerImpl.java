package de.mhus.lib.cao;

/**
 * <p>DriverManagerImpl class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DriverManagerImpl extends CaoDriverManager {

	/**
	 * <p>register.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public void register(CaoDriver driver) {
		schemes.put(driver.getScheme(), driver);
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public void unregister(CaoDriver driver) {
		schemes.remove(driver.getScheme());
	}

	/**
	 * <p>unregister.</p>
	 *
	 * @param scheme a {@link java.lang.String} object.
	 */
	public void unregister(String scheme) {
		schemes.remove(schemes);
	}

}
