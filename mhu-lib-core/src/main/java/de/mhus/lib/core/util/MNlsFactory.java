package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.directory.ClassLoaderResourceProvider;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.directory.ResourceNode;

public class MNlsFactory extends MNlsBundle {

	@SuppressWarnings("unused")
	private ResourceNode config;
	
	public MNlsFactory() {
		this(null);
//		forkBase();
	}
	
	public MNlsFactory(ResourceNode config) {
		this.config = config;
	}
	
	public MNls create(Object owner) {
		return load(null, null, toResourceName(owner), null);
	}
	
	public MNls load(Class<?> owner) {
		return load(null, owner, null, null);
	}
	
	public static String toResourceName(Object owner) {
		if (owner == null) return null;
		if (owner instanceof String)
			return (String)owner;
		return MSystem.getClassName(owner).replace('.', '/');
	}

	public MNls load(Class<?> owner, Locale locale) {
		return load(null, owner, null, locale == null ? null : locale.toString());
	}

	public MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale) {
		return load(res,owner,resourceName,locale, true);
	}
	
	public MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale, boolean searchAlternatives) {
		return load(res,owner,resourceName,locale,searchAlternatives,0);
	}
	
	protected MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale, boolean searchAlternatives, int level) {
		if (level > 50) return null;
		try {
			// if (res == null) res = base(MDirectory.class);
			
			if (resourceName == null) {
				if (owner.getCanonicalName() != null)
					resourceName = owner.getCanonicalName().replace('.', '/');
				else
					resourceName = owner.getEnclosingClass().getCanonicalName().replace('.', '/');
			}
			
			if ( res == null ) {
				res = findResourceProvider(owner);
			}

			if (locale == null)
				locale = getDefaultLocale();
			
			InputStream is = null;
			Properties properties = new Properties();
			
			is = res.getResourceByPath(locale.toString() + "/" + resourceName + ".properties" ).getInputStream();
			String prefix = getResourcePrefix();
			
			if (searchAlternatives) {

				if (prefix != null && is==null)
					is = res.getResourceByPath(prefix + "/" + getDefaultLocale()  + "/" + resourceName + ".properties" ).getInputStream();
				if (is==null)
					is = res.getResourceByPath(getDefaultLocale()  + "/" + resourceName + ".properties" ).getInputStream();
				if (prefix != null && is==null)
					is = res.getResourceByPath(prefix + "/" + resourceName + ".properties" ).getInputStream();
				if (is==null)
					is = res.getResourceByPath(resourceName + ".properties" ).getInputStream();
			
			}
			
			if (is != null) {
				log().t("Load Resource",resourceName,locale);
				InputStreamReader r = new InputStreamReader(is, MString.CHARSET_UTF_8);
				properties.load(r);
				is.close();
				
				for (String include : properties.getProperty(".include", "").split(",")) {
					include = include.trim();
					MNls parent = load(null, null, include, locale, false, level+1);
					if (parent != null) {
						for ( Map.Entry<Object, Object> entry : parent.properties.entrySet()) {
							if (!properties.containsKey(entry.getKey()))
								properties.put(entry.getKey(), entry.getValue());
						}
					}
				}
				
				return new MNls(properties,"");
			} else {
				log().d("Resource not found",resourceName,locale);
			}
			
		} catch (Throwable e) {
			log().i(e);
		}

		return new MNls();
	}
	
	protected String getResourcePrefix() {
		return null;
	}

	protected MResourceProvider<?> findResourceProvider(Class<?> owner) {
		if (owner != null)
			return new ClassLoaderResourceProvider(owner.getClassLoader());
		else
			return new ClassLoaderResourceProvider();
	}

	public String getDefaultLocale() {
		return Locale.getDefault().toString();
	}

	public MNls load(InputStream is) {
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			log().i(e);
		}
		return new MNls(properties,"");
	}
	
	public static MNlsFactory lookup(Object owner) {
		return MApi.lookup(MNlsFactory.class);
	}

	@Override
	public MNls createNls(String locale) {
		return load(null, null, getPath(), locale, false);
	}

}
