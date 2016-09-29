package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>MNls class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MNls extends AbstractProperties {
	
	protected Properties properties = null;
	protected String prefix = "";

	/**
	 * <p>Constructor for MNls.</p>
	 */
	public MNls() {
		this(new Properties(), "");
	}
	
	/**
	 * <p>Constructor for MNls.</p>
	 *
	 * @param properties a {@link java.util.Properties} object.
	 * @param prefix a {@link java.lang.String} object.
	 */
	public MNls(Properties properties, String prefix) {
		this.properties = properties;
		this.prefix = prefix == null || "".equals(prefix) ? "" : prefix + ".";
	}
	
	/**
	 * <p>find.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @param strings a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String find(String in, String ... strings ) {
		if (strings == null || strings.length == 0)
			return find(in,(Map<String, Object>)null);
		HashMap<String, Object> attr = new HashMap<String, Object>();
		for (int i = 0; i < strings.length; i++)
			attr.put(String.valueOf(i), strings[i]);
		return find(in,attr);
	}
	
	/**
	 * <p>find.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String find(String in) {
		return find(in,(Map<String, Object>)null);
	}

	/**
	 * <p>find.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @param attributes a {@link java.util.Map} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String find(String in, Map<String, Object> attributes) {
		return find(this,in, attributes);
	}

	/**
	 * <p>find.</p>
	 *
	 * @param provider a {@link de.mhus.lib.core.util.MNlsProvider} object.
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String find(MNlsProvider provider, String in) {
		return find(provider == null ? null : provider.getNls(), in, null);
	}

	/**
	 * <p>find.</p>
	 *
	 * @param nls a {@link de.mhus.lib.core.util.MNls} object.
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String find(MNls nls, String in) {
		return find(nls, in, null);
	}
	
	/**
	 * <p>find.</p>
	 *
	 * @param nls a {@link de.mhus.lib.core.util.MNls} object.
	 * @param in a {@link java.lang.String} object.
	 * @param attributes a {@link java.util.Map} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String find(MNls nls, String in, Map<String, Object> attributes) {
		String def = null;
		if (in == null) return "";
		int pos = in.indexOf("=");
		if (pos == 0) {  // no key defined
			return in.substring(1);
		}
		if (pos > 0) { // default defined
			def = in.substring(pos+1);
			in  = in.substring(0,pos);
		}
		
		if (def == null) def = in;
		if (nls == null) return def;
		
		try {
			String ret = nls.getString(in,def);
			if (ret == null) return def;
			
			if (attributes != null && ret.indexOf('$') >=0) {
				ret = StringCompiler.compile(ret).execute(attributes);
			}
			
			return ret;
		} catch (MException e) {
		}

		return in;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return properties.get( prefix + name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(prefix + name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
		properties.remove(prefix + key);
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
		properties.put(prefix + key, value );
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
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
	
	/**
	 * <p>createSubstitute.</p>
	 *
	 * @param prefix a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls createSubstitute(String prefix) {
		if (prefix == null) return this;
		return new MNls(properties, this.prefix + prefix);
	}
	
	/**
	 * <p>lookup.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public static MNls lookup(Object owner) {
		MNlsFactory factory = MNlsFactory.lookup(owner);
		if (factory != null)
			return factory.load(owner.getClass());
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return properties.size();
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Object> values() {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new NotSupportedException();
	}

}
