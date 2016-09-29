package de.mhus.lib.core.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.logging.Log;

/**
 * This class loader is a distributor. You can dynamically change the list of child loaders.
 *
 * The class loader is thread safe.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DynamicClassLoader extends ClassLoader {
		
	public enum RESULT {NEXT,OWN,FORWARD};
	/** Constant <code>log</code> */
	public static Log log = Log.getLog(DynamicClassLoader.class);
	
	protected String name = null;
	@SuppressWarnings("rawtypes")
	protected LinkedList<MResourceProvider> classLoaders = new LinkedList<MResourceProvider>();
	protected Rule[] rules = null;
	protected RESULT defaultRule = RESULT.NEXT;
	
	//protected ClassLoader last;
	
	/**
	 * <p>Constructor for DynamicClassLoader.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public DynamicClassLoader(String name) {
		//super(new EmptyClassLoader());
		//last = getSystemClassLoader();
		this.name = name;
	}
	
	/**
	 * <p>Constructor for DynamicClassLoader.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param parent a {@link java.lang.ClassLoader} object.
	 */
	public DynamicClassLoader(String name, ClassLoader parent) {
		super(parent);
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * <p>Setter for the field <code>rules</code>.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public void setRules(List<Rule> list ) {
		rules = list.toArray(new Rule[list.size()]);
	}
	
	/**
	 * <p>addRule.</p>
	 *
	 * @param rule a {@link de.mhus.lib.core.lang.DynamicClassLoader.Rule} object.
	 */
	public void addRule(Rule rule) {
		LinkedList<Rule> list = new LinkedList<Rule>();
		if (rules != null) {
			for (Rule r : rules)
				list.add(r);
		}
		list.add(rule);
		setRules(list);
	}
	
	/**
	 * <p>Setter for the field <code>defaultRule</code>.</p>
	 *
	 * @param rule a {@link de.mhus.lib.core.lang.DynamicClassLoader.RESULT} object.
	 */
	public void setDefaultRule(RESULT rule) {
		defaultRule = rule;
	}
	
	/** {@inheritDoc} */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		log.t("ask for",this,name);
		
		if (rules != null) {
			for (Rule rule : rules) {
				RESULT res = rule.check(name);
				switch (res) {
				case OWN:
					return findAndOwnClass(name);
				case FORWARD:
					return super.findClass(name);
				default:
					break;
				}
			}
		}
		
		if (defaultRule == RESULT.OWN)
			return findAndOwnClass(name);
		if (defaultRule == RESULT.FORWARD)
			return super.findClass(name);

		String resName = name.replaceAll("\\.", "/") + ".class";
		for (@SuppressWarnings("rawtypes") MResourceProvider cl : classLoaders) {
			try {
				InputStream res = cl.getResource(resName).getInputStream();
				if (res != null) {
					log.t("loaded class",this,cl,name);
					return toClass(name,res);
				}
			} catch (Exception e) {
				log.t(name,e);
			}
		}
		
		return super.findClass(name);
	}
	
	/**
	 * <p>findAndOwnClass.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.Class} object.
	 * @throws java.lang.ClassNotFoundException if any.
	 */
	public Class<?> findAndOwnClass(String name) throws ClassNotFoundException {
		
		if (name.startsWith("java.") || name.startsWith("javax.") ) {
			return super.loadClass(name);
		}
		
		String resName = name.replaceAll("\\.", "/") + ".class";
		for (@SuppressWarnings("rawtypes") MResourceProvider cl : classLoaders) {
			try {
				InputStream res = cl.getResource(resName).getInputStream();
				if (res != null) {
					log.t("loaded class",this,cl,name);
					return toClass(name,res);
				}
			} catch (Exception e) {
				log.t(name,e);
			}
		}
		return super.findClass(name);
	}

	private Class<?> toClass(String name, InputStream is) throws ClassNotFoundException {
		try {
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
			byte buffer[] = new byte[1024];
//			int i = 0;
			do {
				int j = is.read(buffer, 0, buffer.length);
				if (j >= 0) {
//					i += j;
					stream.write(buffer, 0, j);
				} else {
					break;

				}
			} while (true);
			byte[] binary = stream.toByteArray();
			return defineClass(name,binary,0,binary.length);
		} catch (IOException e) {
			throw new ClassNotFoundException(name,e);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected URL findResource(String name) {
		log.t("resource",this,name);
		for (@SuppressWarnings("rawtypes") MResourceProvider cl : classLoaders) {
			try {
				URL res = cl.getResource(name).getUrl();
				if (res != null) {
					res.openStream().close();
					log.t("loaded resource",this,cl,name);
					return res;
				}
			} catch (Exception e) {}
		}
		return super.findResource(name);
    }

	// --- Methods to handle list
	
	/**
	 * <p>add.</p>
	 *
	 * @param e a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @return a boolean.
	 */
	public boolean add(@SuppressWarnings("rawtypes") MResourceProvider e) {
		return classLoaders.add(e);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param o a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @return a boolean.
	 */
	public boolean remove(@SuppressWarnings("rawtypes") MResourceProvider o) {
		return classLoaders.remove(o);
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		classLoaders.clear();
	}

	/**
	 * <p>add.</p>
	 *
	 * @param index a int.
	 * @param element a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 */
	public void add(int index, @SuppressWarnings("rawtypes") MResourceProvider element) {
		classLoaders.add(index, element);
	}
	
//	public void doSetupFromConfig(IConfig config) {
//		
//		for (IConfig sub : config.getConfigBundle("resource")) {
//			if (sub.isProperty("jar")) {
//				String jar = sub.getExtracted("jar");
//				log.d("add loader for jar",this,jar);
//				try {
//					ZipResourceProvider loader = new ZipResourceProvider(new File(jar));
//					add(loader);
//				} catch (Exception e) {
//					log.w("can't load jar",this,jar);
//				}
//			} else
//			if (sub.isProperty("path")) {
//				String path = sub.getExtracted("path");
//				log.d("add loader for path",this,path);
//				try {
//					PathResourceProvider loader = new PathResourceProvider(new File(path));
//					add(loader);
//				} catch (Exception e) {
//					log.w("can't load path",this,path);
//				}
//				
//			}
//		}
		
//		
//		IConfig cRules = config.getConfig("rules");
//		if (cRules != null) {
//			
//			String def = cRules.getExtracted("default","OWN").toUpperCase();
//			log.d("default rule",this,def);
//			setDefaultRule(RESULT.valueOf(def));
//			
//			LinkedList<Rule> r = new LinkedList<DynamicClassLoader.Rule>();
//			for (IConfig sub : cRules.getConfigBundle("rule")) {
//				log.d("add rule",this,sub.getExtracted("pattern"),sub.getExtracted("result","FORWARD"));
//				r.add(new Rule(sub.getExtracted("pattern"), RESULT.valueOf(sub.getExtracted("result","FORWARD").toUpperCase()) ));
//			}
//			setRules(r);
//		}
//	}
	
	public static class Rule {
		
		private String pattern;
		private RESULT result;

		public Rule(String pattern,RESULT result) {
			this.pattern = pattern;
			this.result = result;
		}
		
		public RESULT check(String name) {
			if (MString.compareFsLikePattern(name, pattern))
				return result;
			return RESULT.NEXT;
		}
		
		
	}
	
}
