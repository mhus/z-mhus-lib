package de.mhus.lib.form.definition;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;

/**
 * <p>FmDefaultSources class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmDefaultSources extends DefAttribute {

	private String dbPackage = DataSource.PACKAGE_PERSISTENT;
	private String memPackage = DataSource.PACKAGE_MEM;

	/**
	 * <p>Constructor for FmDefaultSources.</p>
	 */
	public FmDefaultSources() {
		this(null,null);
	}
	
	/**
	 * <p>Constructor for FmDefaultSources.</p>
	 *
	 * @param memPackage a {@link java.lang.String} object.
	 * @param dbPackage a {@link java.lang.String} object.
	 */
	public FmDefaultSources(String memPackage, String dbPackage) {
		super(null, null);
		if (dbPackage != null) this.dbPackage = dbPackage;
		if (memPackage != null) this.memPackage = memPackage;
	}

	/** {@inheritDoc} */
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
