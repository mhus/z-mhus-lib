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

public class MNlsFactory extends MObject {

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
		try {
			installBase();
			return load(null, owner.getClass(),null, null);
		} finally {
			leaveBase();
		}
	}
	
	public MNls load(Class<?> owner) {
		return load(null, owner, null, null);
	}
	
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
			log().info(e);
		}

		return new MNls();
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
		Base base = Base.lookup(owner);
		if (base != null) {
			return base.base(MNlsFactory.class);
		}
		return null;
	}

}
