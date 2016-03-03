package de.mhus.lib.core;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.SetCast;

/**
 * <p>MProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MProperties extends IProperties implements Externalizable {

	protected Properties properties = null;
	
	/**
	 * <p>Constructor for MProperties.</p>
	 */
	public MProperties() {
		this(new Properties());
	}
	
	/**
	 * <p>Constructor for MProperties.</p>
	 *
	 * @param values a {@link java.lang.String} object.
	 */
	public MProperties(String ... values) {
		this(new Properties());
		if (values != null) {
			for (int i = 0; i < values.length; i+=2) {
				if (i+1 < values.length)
					setString(values[i], values[i+1]);
			}
		}
	}
	
	/**
	 * <p>Constructor for MProperties.</p>
	 *
	 * @param config a {@link java.util.Dictionary} object.
	 */
	public MProperties(Dictionary<?, ?> config) {
		this.properties = new Properties();
		for (Enumeration<?> enu = config.keys(); enu.hasMoreElements();) {
			Object next = enu.nextElement();
			this.properties.put(String.valueOf( next ), config.get(next));
		}
	}
	
	/**
	 * <p>Constructor for MProperties.</p>
	 *
	 * @param in a {@link java.util.Map} object.
	 */
	public MProperties(Map<?, ?> in) {
		this.properties = new Properties();
		for (Map.Entry<?, ?> e : in.entrySet())
			if (e.getKey() != null && e.getValue() != null)
				this.properties.put(String.valueOf( e.getKey() ), e.getValue());
	}
	
	/**
	 * <p>Constructor for MProperties.</p>
	 *
	 * @param properties a {@link java.util.Properties} object.
	 */
	public MProperties(Properties properties) {
		this.properties = properties;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value );
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this, properties);
	}
	
	/** {@inheritDoc} */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject( properties);
	}

	/** {@inheritDoc} */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		properties = (Properties) in.readObject();
	}
	
	/**
	 * <p>explodeToMProperties.</p>
	 *
	 * @param properties an array of {@link java.lang.String} objects.
	 * @return a {@link de.mhus.lib.core.MProperties} object.
	 */
	public static MProperties explodeToMProperties(String[] properties) {
		MProperties p = new MProperties();
		if (properties != null) {
			for (String i : properties) {
				if (i != null) {
					int idx = i.indexOf('=');
					if (idx >= 0) {
						p.setProperty(i.substring(0,idx).trim(),i.substring(idx+1));
					}
				}
			}
		}
		return p;
	}
	
	/**
	 * <p>explodeToProperties.</p>
	 *
	 * @param properties an array of {@link java.lang.String} objects.
	 * @return a {@link java.util.Properties} object.
	 */
	public static Properties explodeToProperties(String[] properties) {
		Properties p = new Properties();
		if (properties != null) {
			for (String i : properties) {
				if (i != null) {
					int idx = i.indexOf('=');
					if (idx >= 0) {
						p.setProperty(i.substring(0,idx).trim(),i.substring(idx+1));
					}
				}
			}
		}
		return p;
	}

	/**
	 * <p>load.</p>
	 *
	 * @param fileName a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.MProperties} object.
	 * @since 3.2.9
	 */
	public static MProperties load(String fileName) {
		Properties p = new Properties();
		try {
			File f = new File(fileName);
			if (f.exists() && f.isFile()) {
				FileInputStream is = new FileInputStream(f);
				p.load(is);
			}
		} catch (Throwable t) {
			MLogUtil.log().d(fileName, t);
		}
		MProperties out = new MProperties(p);
		return out;
	}
}
