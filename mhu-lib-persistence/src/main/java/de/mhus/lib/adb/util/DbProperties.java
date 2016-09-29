package de.mhus.lib.adb.util;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.MException;

/**
 * A simple key - value properties implementation.
 * 
 * @author mikehummel
 *
 */
public class DbProperties {

	private DbManager manager;
	private String registryName;
	@SuppressWarnings("unused")
	private String dump;

	public DbProperties(DbManager manager, String registryName) {
		this.manager = manager;
		this.registryName = registryName;
	}

	public String get(String key) throws MException {
		Property prop = (Property) manager.getObject(registryName,key);
		if (prop == null) return null;
		return prop.getValue();
	}

	public String set(String key, String value) throws MException {
		Property prop = (Property) manager.getObject(registryName,key);
		if (prop == null) {
			prop = new Property();
			prop.setKey(key);
			prop.setValue(value);
			manager.createObject(registryName, prop);
			return null;
		}
		String oldValue = prop.getValue();
		prop.setValue(value);
		manager.saveObject(registryName,prop);
		dump = null;
		return oldValue;
	}

	public String remove(String key) throws MException {
		Property prop = (Property) manager.getObject(registryName,key);
		if (prop == null) {
			return null;
		}
		String oldValue = prop.getValue();
		manager.deleteObject(registryName,prop);
		dump = null;
		return oldValue;
	}

	//	public String toString() {
	//		if (dump == null) {
	//			try {
	//				TreeMap<String, String> tree = new TreeMap<String, String>();
	//				DbCollection<Property> res = manager.getByQualification(null, new Property(), registryName, "", null);
	//				for (Property p : res) tree.put(p.getKey(), p.getValue());
	//				res.close();
	//				dump = tree.toString() + "@DbProperties";
	//			} catch (MException e) {
	//				dump = e.toString() + "@DbProperties";
	//			}
	//		}
	//		return dump;
	//	}
}
