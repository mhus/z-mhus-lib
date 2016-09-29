package de.mhus.lib.jpa;

import java.util.Properties;

import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public class JpaProperties extends Properties {

	protected JpaSchema schema;
	protected ResourceNode config;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public JpaProperties() {
		super();
		config = new HashConfig();
	}

	public JpaProperties(Properties arg0) {
		super(arg0);
		config = new HashConfig();
	}

	public JpaSchema getSchema() {
		return schema;
	}

	public void setSchema(JpaSchema schema) {
		this.schema = schema;
	}

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

	public ResourceNode getConfig() {
		return config;
	}
}
