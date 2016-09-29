package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ClassLoaderResourceProvider;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.Base;

/**
 * <p>MNlsFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MNlsFactory extends MNlsBundle {

	@SuppressWarnings("unused")
	private ResourceNode config;
	
	/**
	 * <p>Constructor for MNlsFactory.</p>
	 */
	public MNlsFactory() {
		this(null);
//		forkBase();
	}
	
	/**
	 * <p>Constructor for MNlsFactory.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public MNlsFactory(ResourceNode config) {
		this.config = config;
	}
	
	/**
	 * <p>create.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls create(Object owner) {
		return load(null, null, toResourceName(owner), null);
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param owner a {@link java.lang.Class} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls load(Class<?> owner) {
		return load(null, owner, null, null);
	}
	
	/**
	 * <p>toResourceName.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.3.0
	 */
	public static String toResourceName(Object owner) {
		if (owner == null) return null;
		if (owner instanceof String)
			return (String)owner;
		if (owner instanceof Class)
			return ((Class<?>)owner).getCanonicalName().replace('.', '/');
		return owner.getClass().getCanonicalName().replace('.', '/');
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param res a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @param owner a {@link java.lang.Class} object.
	 * @param resourceName a {@link java.lang.String} object.
	 * @param locale a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale) {
		return load(res,owner,resourceName,locale, true);
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param res a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @param owner a {@link java.lang.Class} object.
	 * @param resourceName a {@link java.lang.String} object.
	 * @param locale a {@link java.lang.String} object.
	 * @param searchAlternatives a boolean.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 * @since 3.3.0
	 */
	public MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale, boolean searchAlternatives) {
		return load(res,owner,resourceName,locale,searchAlternatives,0);
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param res a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @param owner a {@link java.lang.Class} object.
	 * @param resourceName a {@link java.lang.String} object.
	 * @param locale a {@link java.lang.String} object.
	 * @param searchAlternatives a boolean.
	 * @param level a int.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 * @since 3.3.0
	 */
	protected MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale, boolean searchAlternatives, int level) {
		if (level > 50) return null;
		try {
			// if (res == null) res = base(MDirectory.class);
			
			if (resourceName == null)
				resourceName = owner.getCanonicalName().replace('.', '/');
			
			if ( res == null ) {
				if (owner != null)
					res = new ClassLoaderResourceProvider(owner.getClassLoader());
				else
					res = new ClassLoaderResourceProvider();
			}

			if (locale == null)
				locale = getDefaultLocale();
			
			InputStream is = null;
			Properties properties = new Properties();
			
			is = res.getResource(locale.toString() + "/" + resourceName + ".properties" ).getInputStream();
			if (searchAlternatives) {

				if (is==null)
					is = res.getResource(getDefaultLocale()  + "/" + resourceName + ".properties" ).getInputStream();
				if (is==null)
					is = res.getResource(resourceName + ".properties" ).getInputStream();
			
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

			return null;
			
		} catch (Exception e) {
			log().i(e);
		}

		return new MNls();
	}
	
	/**
	 * <p>getDefaultLocale.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDefaultLocale() {
		return Locale.getDefault().toString();
	}

	/**
	 * <p>load.</p>
	 *
	 * @param is a {@link java.io.InputStream} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls load(InputStream is) {
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			log().i(e);
		}
		return new MNls(properties,"");
	}
	
	/**
	 * <p>lookup.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.util.MNlsFactory} object.
	 */
	public static MNlsFactory lookup(Object owner) {
		return MSingleton.baseLookup(owner,MNlsFactory.class);
	}

	/** {@inheritDoc} */
	@Override
	public MNls createNls(String locale) {
		return load(null, null, getPath(), locale, false);
	}

}
