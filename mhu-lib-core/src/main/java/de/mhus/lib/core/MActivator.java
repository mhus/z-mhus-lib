package de.mhus.lib.core;

import java.io.InputStream;
import java.net.URL;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.annotations.activator.ObjectFactory;
import de.mhus.lib.annotations.lang.Prototype;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.Injector;
import de.mhus.lib.core.lang.InjectorList;

/**
 * <p>Abstract MActivator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(DefaultActivator.class)
public abstract class MActivator extends ClassLoader  {

//	protected static StaticBase base = new StaticBase();
	
	protected InjectorList injector;

	private boolean destroyed = false;

	/**
	 * <p>Constructor for MActivator.</p>
	 */
	public MActivator() {
//		this(Thread.currentThread().getContextClassLoader());
		this(MActivator.class.getClassLoader());
	}
	
	/**
	 * <p>Constructor for MActivator.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public MActivator(ClassLoader loader) {
		super(loader);
	}
			
	/**
	 * <p>createObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws java.lang.Exception if any.
	 */
	public <T> T createObject(Class<T> ifc, String name) throws Exception {
		return (T)createObject(ifc,name,null,null);
	}
	
	/**
	 * <p>createObject.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public Object createObject(String name) throws Exception {
		return createObject(name, null, null);
	}
	
	/**
	 * <p>mapName.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	protected abstract Object mapName(String name);
	
	/**
	 * <p>createObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 * @param classes an array of {@link java.lang.Class} objects.
	 * @param objects an array of {@link java.lang.Object} objects.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws java.lang.Exception if any.
	 */
	@SuppressWarnings("unchecked")
	public <T> T createObject(Class<T> ifc, String name, Class<?>[] classes, Object[] objects ) throws Exception {
		return (T)createObject(ifc.getCanonicalName() + ":" + name,classes,objects);
	}

	/**
	 * <p>createObject.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param classes an array of {@link java.lang.Class} objects.
	 * @param objects an array of {@link java.lang.Object} objects.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public Object createObject(String name, Class<?>[] classes, Object[] objects ) throws Exception {
		
		Object obj = mapName(name);
		
		Class<?> clazz = null;
		if (obj instanceof String) {
			clazz = loadClass((String)obj);
		} else
		if (obj instanceof Class) {
			clazz = (Class<?>)obj;
		} else
			clazz = obj.getClass();
		
		DefaultImplementation defaultImplementation = clazz.getAnnotation(DefaultImplementation.class);
		if (defaultImplementation != null) {
			clazz = defaultImplementation.value();
		}
		
		Object out = null;
		
		DefaultFactory factoryClazz = clazz.getAnnotation(DefaultFactory.class);
		if (factoryClazz != null) {
			ObjectFactory inst = getObject(factoryClazz.value());
			if (inst != null) {
				out = inst.create(clazz,classes,objects);
			}
		}
		if (out == null) {
			if (classes == null || objects == null)
				out =  clazz.newInstance();
			else
				out = clazz.getConstructor(classes).newInstance(objects);
		}				
		if (injector != null) injector.doInject(out);
		
		return out;
	}
	
	/**
	 * <p>getResourceStream.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.lang.Exception if any.
	 */
	public InputStream getResourceStream(String name) throws Exception {
		Object obj = mapName(name);
				
		if (obj instanceof String)
			return getResourceAsStream((String)obj);
		if (obj instanceof InputStream)
			return (InputStream)obj;
		if (obj instanceof Class)
			return (InputStream) ((Class<?>)obj).newInstance();
		return null;
	}

	/**
	 * <p>getObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param name a {@link java.lang.String} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws java.lang.Exception if any.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> ifc, String name) throws Exception {
		return (T)getObject(ifc.getCanonicalName() + ":" + name);
	}
	
	/**
	 * <p>getObject.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws java.lang.Exception if any.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> ifc) throws Exception {
		return (T)getObject(ifc.getCanonicalName());
	}
	
	/**
	 * <p>getObject.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	public Object getObject(String name) throws Exception {
				
		if (isInstance(name))
			return getInstance(name);
		
		Class<?> clazz = findClass(name);
		Class<?> orgClazz = clazz;
		
		DefaultImplementation defaultImplementation = clazz.getAnnotation(DefaultImplementation.class);
		if (defaultImplementation != null) {
			clazz = defaultImplementation.value();
		}
		DefaultFactory defaultFactory = clazz.getAnnotation(DefaultFactory.class);
		if (defaultFactory != null) {
			ObjectFactory factory = getObject(defaultFactory.value());
			return factory.create(orgClazz, null, null);
		}

		Object obj = clazz.newInstance();
		if (injector != null) injector.doInject(obj);
		
		if (clazz.getAnnotation(Prototype.class) == null && orgClazz.getAnnotation(Prototype.class) == null)
			setInstance(name, obj);
		
		return obj;
	}

	/**
	 * <p>getInstance.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	protected abstract Object getInstance(String name);

	/**
	 * <p>setInstance.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param obj a {@link java.lang.Object} object.
	 */
	protected abstract void setInstance(String name, Object obj);
	
	/**
	 * <p>getClazz.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Class} object.
	 * @throws java.lang.Exception if any.
	 */
	public Class<?> getClazz(String name) throws Exception {
		Object obj = mapName(name);
		if (obj instanceof String)
			return loadClass((String)obj);
		if (obj instanceof Class)
			return (Class<?>)obj;
		return obj.getClass();
		
	}

	/**
	 * <p>getURL.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @return a {@link java.net.URL} object.
	 */
	public URL getURL(String path) {
		return getResource(path);
	}
	
	/**
	 * <p>Getter for the field <code>injector</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.lang.InjectorList} object.
	 */
	public InjectorList getInjector() {
		return injector;
	}

	/**
	 * <p>Setter for the field <code>injector</code>.</p>
	 *
	 * @param injector a {@link de.mhus.lib.core.lang.InjectorList} object.
	 */
	public void setInjector(InjectorList injector) {
		this.injector = injector;
	}

	/**
	 * <p>addInjector.</p>
	 *
	 * @param injector a {@link de.mhus.lib.core.lang.Injector} object.
	 */
	public void addInjector(Injector injector) {
		if (this.injector == null)
			this.injector = new InjectorList();
		this.injector.add(injector);
	}
	
	/**
	 * <p>destroy.</p>
	 */
	public void destroy() {
		destroyed  = true;
	}

	/**
	 * <p>isDestroyed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDestroyed() {
		return destroyed;
	}
	
	/**
	 * <p>isInstance.</p>
	 *
	 * @param ifc a {@link java.lang.Class} object.
	 * @return a boolean.
	 */
	public boolean isInstance(Class<?> ifc) {
		return isInstance(ifc.getCanonicalName());
	}
	
	/**
	 * <p>isInstance.</p>
	 *
	 * @param ifc a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public abstract boolean isInstance(String ifc);
	
	/** {@inheritDoc} */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		
		String n = name;
		if (n.indexOf(':') > 0) n = n.substring(0,n.indexOf(':'));
		
		Object obj = mapName(n);
		if (obj instanceof String) {
			return getParent().loadClass((String)obj);
		} else
		if (obj instanceof Class) {
			return (Class<?>)obj;
		}
		return getParent().loadClass(name);
	}
	
	/** {@inheritDoc} */
	@Override
	protected URL findResource(String name) {
		Object obj = mapName(name);
		if (obj instanceof URL)
			return (URL) obj;
		else
		if (obj instanceof String)
			return getParent().getResource((String)obj);
		
		return getParent().getResource(name);
	}
	
}
