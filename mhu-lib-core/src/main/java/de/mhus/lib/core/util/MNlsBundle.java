package de.mhus.lib.core.util;

import java.util.HashMap;
import java.util.Locale;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract MNlsBundle class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
@DefaultImplementation(MNlsFactory.class)
public abstract class MNlsBundle extends MObject {

	private String path;
	private MNls defaultNls;
	private HashMap<String, Object> cache = new HashMap<>();

	/**
	 * <p>Constructor for MNlsBundle.</p>
	 */
	public MNlsBundle() {}
	
	/**
	 * <p>Constructor for MNlsBundle.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 */
	public MNlsBundle(String path) {
		setPath(path);
	}

	/**
	 * <p>Constructor for MNlsBundle.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 */
	public MNlsBundle(Object owner) {
		setPath(MNlsFactory.toResourceName(owner));
	}

	/**
	 * <p>getNls.</p>
	 *
	 * @param locale a {@link java.util.Locale} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls getNls(Locale locale) {
		if (locale == null) return getDefaultNls();
		String tag = locale.toLanguageTag();
		MNls out = getNls(tag);
		if (out != null) return out;
		tag = locale.getLanguage();
		out = getNls(tag);
		if (out != null) return out;
		return getDefaultNls();
	}
	
	/**
	 * <p>Getter for the field <code>defaultNls</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public synchronized MNls getDefaultNls() {
		if (defaultNls != null) return defaultNls;
		defaultNls = getNls("");
		if (defaultNls == null) defaultNls = new MNls();
		return defaultNls;
	}

	/**
	 * <p>getNls.</p>
	 *
	 * @param locale a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public synchronized MNls getNls(String locale) {
		Object out = cache.get(locale);
		if (out != null) {
			if (out instanceof MNls)
				return (MNls)out;
			return null;
		}
		out = createNls(locale);
		if (out == null) out = new Object();
		cache.put(locale, out);
		if (out instanceof MNls)
			return (MNls)out;
		return null;
	}
	
	/**
	 * Create a NLS for the given path and locale or return null.
	 *
	 * @param locale a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public abstract MNls createNls(String locale);

	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <p>Setter for the field <code>path</code>.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * <p>setOwner.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.util.MNlsBundle} object.
	 */
	public MNlsBundle setOwner(Object owner) {
		setPath(MNlsFactory.toResourceName(owner));
		return this;
	}
	
}
