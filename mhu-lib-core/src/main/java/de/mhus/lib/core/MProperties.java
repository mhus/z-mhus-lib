package de.mhus.lib.core;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Reader;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.SetCast;
import de.mhus.lib.errors.NotSupportedException;

public class MProperties extends AbstractProperties implements Externalizable {

	private static final long serialVersionUID = 1L;
	
	protected Properties properties = null;
	
	public MProperties() {
		this(new Properties());
	}
	
	public MProperties(String ... values) {
		this(new Properties());
		if (values != null) {
			for (int i = 0; i < values.length; i+=2) {
				if (i+1 < values.length)
					setString(values[i], values[i+1]);
			}
		}
	}
	
	public MProperties(Dictionary<?, ?> config) {
		this.properties = new Properties();
		for (Enumeration<?> enu = config.keys(); enu.hasMoreElements();) {
			Object next = enu.nextElement();
			this.properties.put(String.valueOf( next ), config.get(next));
		}
	}
	
	public MProperties(Map<?, ?> in) {
		this.properties = new Properties();
		for (Map.Entry<?, ?> e : in.entrySet())
			if (e.getKey() != null && e.getValue() != null)
				this.properties.put(String.valueOf( e.getKey() ), e.getValue());
	}
	
	public MProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, properties);
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject( properties);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		properties = (Properties) in.readObject();
	}
	
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

	@Override
	public boolean containsValue(Object value) {
		return properties.containsValue(value);
	}

	@Override
	public Collection<Object> values() {
		return properties.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		HashMap<String, Object> wrapper = new HashMap<>();
		for (java.util.Map.Entry<Object, Object> entry : properties.entrySet())
			wrapper.put( String.valueOf( entry.getKey() ), entry.getValue() );
		return wrapper.entrySet();
	}

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

	public static MProperties load(File f) {
		Properties p = new Properties();
		try {
			if (f.exists() && f.isFile()) {
				FileInputStream is = new FileInputStream(f);
				p.load(is);
			}
		} catch (Throwable t) {
			MLogUtil.log().d(f, t);
		}
		MProperties out = new MProperties(p);
		return out;
	}
	
	public static MProperties load(InputStream is) {
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (Throwable t) {
			MLogUtil.log().d(t);
		}
		MProperties out = new MProperties(p);
		return out;
	}
	
	public static MProperties load(Reader is) {
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (Throwable t) {
			MLogUtil.log().d(t);
		}
		MProperties out = new MProperties(p);
		return out;
	}
	
	@Override
	public int size() {
		return properties.size();
	}
	
	public boolean save(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		boolean ret = save(fos);
		fos.close();
		return ret;
	}

	public boolean save(FileOutputStream fos) throws IOException {
		Properties p = new Properties();
		p.putAll(this);
		p.store(fos, "");
		return false;
	}
	
}
