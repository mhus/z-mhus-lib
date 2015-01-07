package de.mhus.lib.form.binding;

import java.util.HashMap;
import java.util.Set;

import de.mhus.lib.core.MString;
import de.mhus.lib.form.DataSource;

public class PackagingDataSource extends DataSource {

	private HashMap<String, DataSource> packages = new HashMap<String, DataSource>();
	private DataSource defaultSource;
	
	@Override
	public Object getProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.getProperty(name);
	}

	protected String fixName(DataSource pack, String name) {
		if (pack != defaultSource) name = MString.afterIndex(name, '.');
		return name;
	}

	protected DataSource getPack(String name) {
		if (name == null) return defaultSource;
		String packName = MString.beforeIndex(name, '.');
		if (packName.length() == 0) return defaultSource;
		DataSource pack = packages.get(packName);
		return pack == null ? defaultSource : pack;
	}

	@Override
	public boolean isProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.isProperty(name);
	}

	@Override
	public void removeProperty(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.removeProperty(name);
	}

	@Override
	public void setProperty(String name, Object value) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.setProperty(name, value);
		if (isConnected()) fireValueChanged(name);
	}

	@Override
	public void setPropertyData(String name, Object value) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		pack.setProperty(name, value);
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public Set<String> keys() {
		return null;
	}

	public DataSource getDefaultSource() {
		return defaultSource;
	}

	public void setDefaultSource(DataSource defaultSource) {
		this.defaultSource = defaultSource;
	}

	public void addPackage(String name, DataSource handler) {
		packages.put(name, handler);
	}
	
	public void removePackage(String name) {
		packages.remove(name);
	}

	@Override
	public boolean isPropertyPossible(String name) {
		DataSource pack = getPack(name);
		name = fixName(pack,name);
		return pack.isPropertyPossible(name);
	}
	
}
