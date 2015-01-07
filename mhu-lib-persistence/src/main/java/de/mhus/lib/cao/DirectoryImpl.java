package de.mhus.lib.cao;

public class DirectoryImpl extends CaoDirectory {

	public void register(String scheme, CaoConnection con) {
		schemes.put(scheme, con);
	}
	
	public void unregister(String scheme) {
		schemes.remove(scheme);
	}
	
}
