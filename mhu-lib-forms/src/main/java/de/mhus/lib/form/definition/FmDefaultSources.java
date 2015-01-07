package de.mhus.lib.form.definition;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;

public class FmDefaultSources extends DefAttribute {

	private String dbPackage = DataSource.PACKAGE_PERSISTENT;
	private String memPackage = DataSource.PACKAGE_MEM;

	public FmDefaultSources() {
		this(null,null);
	}
	
	public FmDefaultSources(String memPackage, String dbPackage) {
		super(null, null);
		if (dbPackage != null) this.dbPackage = dbPackage;
		if (memPackage != null) this.memPackage = memPackage;
	}

	@Override
	public void inject(DefComponent root) throws MException {
		String name = root.getString("name", null);
		if (MString.isEmptyTrim(name)) {
			System.out.println("default source needs a name");
			return;
		}
		new FmSource(DataSource.CONNECTOR_TASK_DATA, dbPackage + "." + name).inject(root);
		new FmSource(DataSource.CONNECTOR_TASK_ENABLED, memPackage + "." + name + "enabled").inject(root);
		new FmSource(DataSource.CONNECTOR_TASK_OPTIONS, dbPackage + "." + name + "options").inject(root);
	}
	
}
