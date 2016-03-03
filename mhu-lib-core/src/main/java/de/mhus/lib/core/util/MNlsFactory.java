package de.mhus.lib.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Properties;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ClassLoaderResourceProvider;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>MNlsFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MNlsFactory extends MObject {

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
		try {
			installBase();
			return load(null, owner.getClass(),null, null);
		} finally {
			leaveBase();
		}
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
	 * <p>load.</p>
	 *
	 * @param res a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @param owner a {@link java.lang.Class} object.
	 * @param resourceName a {@link java.lang.String} object.
	 * @param locale a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls load(MResourceProvider<?> res, Class<?> owner, String resourceName, String locale) {
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
			if (is==null)
				is = res.getResource(getDefaultLocale()  + "/" + resourceName + ".properties" ).getInputStream();
			if (is==null)
				is = res.getResource(resourceName + ".properties" ).getInputStream();

			if (is != null) {
				log().t("Load Resource",resourceName,locale);
				InputStreamReader r = new InputStreamReader(is, MString.CHARSET_UTF_8);
				properties.load(r);
				is.close();
			} else {
				log().d("Resource not found",resourceName,locale);
			}

			return new MNls(properties,"");
			
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
		Base base = Base.lookup(owner);
		if (base != null) {
			return base.lookup(MNlsFactory.class);
		}
		return null;
	}

}
