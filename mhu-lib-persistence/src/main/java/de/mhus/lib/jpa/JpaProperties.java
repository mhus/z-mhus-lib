package de.mhus.lib.jpa;

import java.util.Properties;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * <p>JpaProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JpaProperties extends Properties {

	protected JpaSchema schema;
	protected ResourceNode config;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for JpaProperties.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public JpaProperties(ResourceNode config) {
		super();
		this.config = config;
		// fill from config
		ResourceNode cproperties = config.getNode("properties");
		if (cproperties != null) {
			for (ResourceNode prop : cproperties.getNodes("property")) {
				try {
					setProperty(prop.getExtracted("name"), prop.getExtracted("value"));
				} catch (MException e) {
					throw new MRuntimeException(e);
				}
			}
		}

	}

	/**
	 * <p>Constructor for JpaProperties.</p>
	 */
	public JpaProperties() {
		super();
		config = new HashConfig();
	}

	/**
	 * <p>Constructor for JpaProperties.</p>
	 *
	 * @param arg0 a {@link java.util.Properties} object.
	 */
	public JpaProperties(Properties arg0) {
		super(arg0);
		config = new HashConfig();
	}

	/**
	 * <p>Getter for the field <code>schema</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.jpa.JpaSchema} object.
	 */
	public JpaSchema getSchema() {
		return schema;
	}

	/**
	 * <p>Setter for the field <code>schema</code>.</p>
	 *
	 * @param schema a {@link de.mhus.lib.jpa.JpaSchema} object.
	 */
	public void setSchema(JpaSchema schema) {
		this.schema = schema;
	}

	/**
	 * <p>configureTypes.</p>
	 */
	public void configureTypes() {
		setProperty("openjpa.RuntimeUnenhancedClasses", "supported");

		StringBuffer types = null;
		for (Class<?> type : schema.getObjectTypes()) {
			if (types == null) {
				types = new StringBuffer();
			} else {
				types.append(";");
			}
			types.append( type.getCanonicalName() );
		}
		put("openjpa.MetaDataFactory", "jpa(Types="+types+")");
		put("openjpa.jdbc.SynchronizeMappings",  "buildSchema(ForeignKeys=true)");

	}

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getConfig() {
		return config;
	}
}
