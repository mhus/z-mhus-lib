package de.mhus.lib.cao;

public class DriverManagerImpl extends CaoDriverManager {

	public void register(CaoDriver driver) {
		schemes.put(driver.getScheme(), driver);
	}

	public void unregister(CaoDriver driver) {
		schemes.remove(driver.getScheme());
	}

	public void unregister(String scheme) {
		schemes.remove(schemes);
	}

}
