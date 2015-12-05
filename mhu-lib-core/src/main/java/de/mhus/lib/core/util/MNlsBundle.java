package de.mhus.lib.core.util;

import java.util.HashMap;
import java.util.Locale;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.MObject;

@DefaultImplementation(MNlsFactory.class)
public abstract class MNlsBundle extends MObject {

	private String path;
	private MNls defaultNls;
	private HashMap<String, Object> cache = new HashMap<>();

	public MNlsBundle() {}
	
	public MNlsBundle(String path) {
		setPath(path);
	}

	public MNlsBundle(Object owner) {
		setPath(MNlsFactory.toResourceName(owner));
	}

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
	
	public synchronized MNls getDefaultNls() {
		if (defaultNls != null) return defaultNls;
		defaultNls = getNls("");
		if (defaultNls == null) defaultNls = new MNls();
		return defaultNls;
	}

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
	 * @param locale
	 * @return
	 */
	public abstract MNls createNls(String locale);

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MNlsBundle setOwner(Object owner) {
		setPath(MNlsFactory.toResourceName(owner));
		return this;
	}
	
}
